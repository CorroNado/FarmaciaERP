import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Table    from '@/presentation/components/ui/Table';
import FiApRail, { CICLO_FI_AP_FASES } from '@/presentation/components/fiAp/FiApRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useDispersionesBancarias } from '@/presentation/hooks/useDispersionesBancarias';
import { useCases } from '@/infrastructure';
import { ESTADO_DISPERSION_CIERRE_LABEL } from '@/domain/models/DispersionBancariaCierre';

function fmt(n) {
  return 'S/ ' + (Math.round((n ?? 0) * 100) / 100).toFixed(2);
}

function ErrorBox({ error }) {
  if (!error) return null;
  return (
    <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
      {typeof error === 'string' ? error : 'Ocurrió un error'}
    </div>
  );
}

function Callout({ children, tone = 'default' }) {
  const tones = {
    default: 'bg-emerald-50 border-emerald-700 text-emerald-800',
    warn: 'bg-amber-50 border-amber-500 text-amber-800',
    ok: 'bg-emerald-50 border-emerald-600 text-emerald-800',
  };
  return (
    <div className={`border-l-4 rounded-r-lg px-4 py-3 text-sm ${tones[tone]}`}>
      {children}
    </div>
  );
}

function RuleTag({ children }) {
  return (
    <span className="font-mono text-[10px] bg-amber-100 text-amber-800 px-1.5 py-0.5 rounded mr-1.5">
      {children}
    </span>
  );
}

function LaneNote({ children }) {
  return (
    <div className="flex items-center gap-3 my-4 font-mono text-[11px] uppercase tracking-wide text-slate-400">
      <span className="flex-1 h-px bg-slate-200" />
      {children}
      <span className="flex-1 h-px bg-slate-200" />
    </div>
  );
}

function StepLine({ label, done, mark }) {
  const symbol = mark ?? (done ? '✓' : '○');
  return (
    <div className={`text-[12.6px] py-0.5 ${done ? 'text-emerald-700' : 'text-slate-400'}`}>
      {symbol} {label}
    </div>
  );
}

function EntryBox({ children }) {
  return (
    <div className="border border-dashed border-slate-300 rounded-lg px-3 py-2.5 mt-2.5 bg-white font-mono text-xs text-slate-600">
      {children}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Tarjeta de una dispersión bancaria de cierre (recipe-card del prototipo) — pasos 6.1 a 6.7
// ─────────────────────────────────────────────────────────────────────────
function DispersionCard({
  dispersion, procesando,
  onCompilar, onValidar, onCorregirReenviar, onGenerarArchivo,
  onFirmar, onTransferir, onImportarExtracto, onConciliar,
}) {
  return (
    <div className="border border-slate-200 rounded-xl px-4 py-3.5 mb-3 bg-white">
      <div className="flex justify-between items-center gap-3 flex-wrap">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-900">{dispersion.numero}</span>
          <span className="text-slate-500 text-sm"> · Propuesta {dispersion.numeroPropuestaPago}</span>
          <div className="text-xs text-slate-500 mt-0.5">
            Intento(s) de validación: {dispersion.intentosValidacion}
          </div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(dispersion.montoDispersion)}</div>
          <span className={[
            'inline-block mt-1 text-[10.5px] font-mono px-2 py-0.5 rounded-full border',
            dispersion.obligacionExtinguida
              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
              : 'bg-amber-100 text-amber-800 border-amber-200',
          ].join(' ')}
          >
            {dispersion.obligacionExtinguida ? 'Extinguida' : (ESTADO_DISPERSION_CIERRE_LABEL[dispersion.estado]?.split(' —')[0] ?? dispersion.estado)}
          </span>
        </div>
      </div>

      {/* 6.1 — Compilar Propuesta de Pago (F110) recibida desde la Fase 05 */}
      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Compilar Propuesta de Pago (F110) recibida desde la Fase 05" done={dispersion.propuestaCompilada} />
        {dispersion.pendienteCompilar && (
          <div className="flex justify-end mt-3">
            <Button variant="secondary" loading={procesando} onClick={() => onCompilar(dispersion.id)}>
              <RuleTag>6.1</RuleTag>Compilar Propuesta de Pago
            </Button>
          </div>
        )}
      </div>

      {/* 6.2 — Validar Propuesta de Duplicados / Bloqueos */}
      {dispersion.propuestaCompilada && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine
            label="¿Lote Validado? — Validar Propuesta de Duplicados / Bloqueos"
            done={dispersion.propuestaValidada === true}
            mark={dispersion.propuestaValidada === false ? '✗' : undefined}
          />
          {dispersion.pendienteValidar && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onValidar(dispersion.id)}>
                <RuleTag>6.2</RuleTag>Validar Duplicados / Bloqueos
              </Button>
            </div>
          )}
          {dispersion.propuestaValidada === false && (
            <>
              <Callout tone="warn">Posible transferencia duplicada detectada — requiere corrección del lote.</Callout>
              <div className="flex justify-end mt-3">
                <Button variant="secondary" loading={procesando} onClick={() => onCorregirReenviar(dispersion.id)}>
                  Corregir Errores y Reenviar Lote
                </Button>
              </div>
            </>
          )}
          {dispersion.propuestaValidada === true && (
            <Callout tone="ok">Lote validado — sin duplicados ni bloqueos.</Callout>
          )}
        </div>
      )}

      {/* 6.3 — Generar Archivo Bancario Plano (IDoc) */}
      {dispersion.propuestaValidada === true && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Generar Archivo Bancario Plano (IDoc)" done={dispersion.archivoGenerado} />
          {dispersion.pendienteGenerarArchivo && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onGenerarArchivo(dispersion.id)}>
                <RuleTag>6.3</RuleTag>Generar Archivo Bancario (IDoc)
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 6.4 — Aplicar Firma Digital con Token Bancario */}
      {dispersion.archivoGenerado && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Firma Digital con Token Bancario" done={dispersion.firmado} />
          {dispersion.pendienteFirmar && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onFirmar(dispersion.id)}>
                <RuleTag>6.4</RuleTag>Aplicar Firma Digital (Token)
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 6.5 — Ejecutar Transferencias en Banca Empresa (Token) */}
      {dispersion.firmado && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Ejecutar Transferencias en Banca Empresa (Token)" done={dispersion.transferenciasEjecutadas} />
          {dispersion.pendienteTransferir && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onTransferir(dispersion.id)}>
                <RuleTag>6.5</RuleTag>Ejecutar Transferencias Bancarias
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 6.6 — Importar Extracto Bancario Digital del Día (FF.5) */}
      {dispersion.transferenciasEjecutadas && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Importar Extracto Bancario Digital del Día (FF.5)" done={dispersion.extractoImportado} />
          {dispersion.pendienteImportarExtracto && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onImportarExtracto(dispersion.id)}>
                <RuleTag>6.6</RuleTag>Importar Extracto Bancario (FF.5)
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 6.7 — Conciliar Cuentas Puente Financieras y Compensar Cuenta Transitoria */}
      {dispersion.extractoImportado && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Conciliar Cuentas Puente Financieras y Compensar Cuenta Transitoria" done={dispersion.conciliado} />
          {dispersion.pendienteConciliar && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onConciliar(dispersion.id)}>
                <RuleTag>6.7</RuleTag>Conciliar Cuentas Puente y Compensar
              </Button>
            </div>
          )}
          {dispersion.conciliado && (
            <>
              <EntryBox>
                Asiento contable — (Debe) Proveedores / Laboratorio {fmt(dispersion.montoDispersion)} / (Haber) Banco Cuenta Corriente {fmt(dispersion.montoDispersion)}
              </EntryBox>
              <Callout tone="ok">
                <b>Obligación con Proveedor Extinguida.</b> Ciclo FI-AP finalizado exitosamente para el período.
              </Callout>
            </>
          )}
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Flujo de dispersión bancaria y conciliación de cierre (Fase 06)
// ─────────────────────────────────────────────────────────────────────────
function DispersionesBancariasTab() {
  const {
    dispersiones, loading, procesandoId, error,
    cargarDispersiones, iniciarDispersion, compilarPropuestaPago, validarPropuestaDuplicados,
    corregirErroresYReenviarLote, generarArchivoBancario, aplicarFirmaDigital,
    ejecutarTransferenciasBancarias, importarExtractoBancario, conciliarCuentasPuente,
  } = useDispersionesBancarias();

  const [propuestasConcluidas, setPropuestasConcluidas] = useState([]);
  const [loadingPropuestas, setLoadingPropuestas] = useState(false);
  const [localError, setLocalError] = useState('');

  const cargarPropuestasConcluidas = useCallback(async () => {
    setLoadingPropuestas(true);
    try {
      const data = await useCases.propuestaPago.getAll.execute();
      setPropuestasConcluidas(data.filter((p) => p.archivosGenerados));
    } catch (err) {
      setLocalError(err.response?.data ?? err.message ?? 'Error al obtener las propuestas concluidas en Fase 05');
    } finally {
      setLoadingPropuestas(false);
    }
  }, []);

  useEffect(() => {
    cargarDispersiones();
    cargarPropuestasConcluidas();
  }, [cargarDispersiones, cargarPropuestasConcluidas]);

  const propuestasSinDispersion = useMemo(
    () => propuestasConcluidas.filter(
      (p) => !dispersiones.some((d) => d.propuestaPagoAutomaticaId === p.id),
    ),
    [propuestasConcluidas, dispersiones],
  );

  async function handleIniciar(propuestaPagoAutomaticaId) {
    setLocalError('');
    const nueva = await iniciarDispersion(propuestaPagoAutomaticaId);
    if (!nueva) setLocalError(error ?? 'No se pudo iniciar la dispersión bancaria');
  }

  const todasConcluidas = dispersiones.length > 0 && dispersiones.every((d) => d.obligacionExtinguida);

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AP6-01 · SAP FI-AP</span>
        La propuesta de pago se valida contra duplicados, se firma digitalmente y se transfiere a través de
        banca empresa. El cierre concluye al importar el extracto bancario y compensar la cuenta transitoria
        contra el saldo de proveedores.
      </div>

      <ErrorBox error={localError || error} />

      {/* Sistema ERP — propuestas concluidas en Fase 05 listas para iniciar la dispersión */}
      <div>
        <LaneNote>Sistema ERP</LaneNote>
        <Callout>
          Propuestas concluidas en Fase 05 — {loadingPropuestas
            ? 'buscando propuestas concluidas...'
            : `${propuestasSinDispersion.length} propuesta(s) pendiente(s) de iniciar la dispersión bancaria de cierre.`}
        </Callout>

        {propuestasSinDispersion.length > 0 && (
          <div className="mt-3 flex flex-col gap-2">
            {propuestasSinDispersion.map((p) => (
              <div key={p.id} className="flex items-center justify-between gap-3 border border-slate-200 rounded-lg px-3.5 py-2.5 flex-wrap">
                <div className="text-sm">
                  <span className="font-mono font-semibold text-emerald-900">{p.numero}</span>
                  <span className="text-slate-500"> · Monto {fmt(p.montoPropuesta)}</span>
                </div>
                <Button
                  variant="secondary"
                  loading={procesandoId === 'nuevo'}
                  onClick={() => handleIniciar(p.id)}
                >
                  Iniciar Dispersión Bancaria
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Comprador / Analista de AP / Gerente de Finanzas — validación, firma, transferencia y cierre */}
      <div>
        <LaneNote>Comprador · Analista de AP · Gerente de Finanzas</LaneNote>
        {loading ? (
          <div className="text-sm text-slate-400 py-6 text-center">Cargando dispersiones bancarias...</div>
        ) : dispersiones.length === 0 ? (
          <div className="text-sm text-slate-400 py-6 text-center">
            Aún no hay dispersiones bancarias iniciadas. Inicia la dispersión a partir de una propuesta concluida en Fase 05.
          </div>
        ) : (
          dispersiones.map((d) => (
            <DispersionCard
              key={d.id}
              dispersion={d}
              procesando={procesandoId === d.id}
              onCompilar={compilarPropuestaPago}
              onValidar={validarPropuestaDuplicados}
              onCorregirReenviar={corregirErroresYReenviarLote}
              onGenerarArchivo={generarArchivoBancario}
              onFirmar={aplicarFirmaDigital}
              onTransferir={ejecutarTransferenciasBancarias}
              onImportarExtracto={importarExtractoBancario}
              onConciliar={conciliarCuentasPuente}
            />
          ))
        )}
      </div>

      {dispersiones.length > 0 && (
        todasConcluidas ? (
          <div>
            <LaneNote>Sistema ERP</LaneNote>
            <Callout tone="ok">
              <RuleTag>6.7</RuleTag>
              Obligación con proveedores extinguida. Ciclo FI-AP finalizado exitosamente para el período.
            </Callout>
            <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
              <button onClick={() => cargarDispersiones()} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                Actualizar panel
              </button>
            </div>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Completa el flujo de cada dispersión para cerrar el ciclo FI-AP.</p>
        )
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de dispersiones bancarias
// ─────────────────────────────────────────────────────────────────────────
function HistorialDispersionesTab() {
  const [dispersiones, setDispersiones] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Dispersión' },
    { key: 'numeroPropuestaPago', label: 'Propuesta (Fase 05)' },
    { key: 'montoDispersion', label: 'Monto', render: (val) => fmt(val) },
    { key: 'intentosValidacion', label: 'Intentos de validación' },
    {
      key: 'propuestaValidada',
      label: 'Validación',
      render: (val) => (val === null ? 'Pendiente' : (val ? 'Sin duplicados' : 'Con observaciones')),
    },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_DISPERSION_CIERRE_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.dispersionBancaria.getAll.execute();
      setDispersiones(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las dispersiones bancarias');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    cargar();
  }, [cargar]);

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{dispersiones.length} dispersiones bancarias registradas</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando dispersiones bancarias...</div>
        ) : (
          <Table columns={columns} data={dispersiones} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 06: Dispersión Bancaria y Conciliación de Cierre
// ─────────────────────────────────────────────────────────────────────────
export default function DispersionBancariaPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiApRail activeStep={5} maxReached={5} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-AP6-01 · SAP FI-AP"
          title="Dispersión Bancaria y Conciliación de Cierre"
          description="La propuesta de pago se valida contra duplicados, se firma digitalmente y se transfiere a través de banca empresa. El cierre concluye al importar el extracto bancario y compensar la cuenta transitoria contra el saldo de proveedores."
          badge="FI-AP"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Dispersión bancaria' },
            { id: 'historial', label: 'Historial' },
          ].map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={[
                'px-4 py-2.5 text-sm font-medium rounded-t-lg border-b-2 -mb-px transition-colors',
                tab === t.id
                  ? 'border-emerald-700 text-emerald-800'
                  : 'border-transparent text-slate-500 hover:text-slate-700',
              ].join(' ')}
            >
              {t.label}
            </button>
          ))}
        </div>

        {tab === 'flujo' ? <DispersionesBancariasTab /> : <HistorialDispersionesTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 06 de {CICLO_FI_AP_FASES.length} — la conciliación de cuentas puente extingue la obligación con proveedores y finaliza el ciclo FI-AP
      </p>
    </div>
  );
}
