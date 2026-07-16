import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Table    from '@/presentation/components/ui/Table';
import FiApRail, { CICLO_FI_AP_FASES } from '@/presentation/components/fiAp/FiApRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { usePropuestasPago } from '@/presentation/hooks/usePropuestasPago';
import { useCases } from '@/infrastructure';
import { ESTADO_PROPUESTA_PAGO_LABEL } from '@/domain/models/PropuestaPagoAutomatica';

// Parámetros de pago por defecto que introduce el Analista de AP en F110 (paso 5.1)
const PARAMETROS_PAGO_DEFAULT = { sociedad: 'FARM-PE01', viaPago: 'Transferencia Bancaria', fechaPago: '2026-07-21' };

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
// Tarjeta de una propuesta de pago (recipe-card del prototipo) — pasos 5.1 a 5.3
// ─────────────────────────────────────────────────────────────────────────
function PropuestaCard({
  propuesta, procesando,
  onIntroducirParametros, onEjecutarPropuesta, onRevisarExcepciones,
  onAjustarReejecutar, onAprobar, onEjecutarPago, onGenerarArchivos,
}) {
  return (
    <div className="border border-slate-200 rounded-xl px-4 py-3.5 mb-3 bg-white">
      <div className="flex justify-between items-center gap-3 flex-wrap">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-900">{propuesta.numero}</span>
          <span className="text-slate-500 text-sm"> · Lote {propuesta.numeroLotePago}</span>
          <div className="text-xs text-slate-500 mt-0.5">
            Intento(s): {propuesta.intentos}
          </div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(propuesta.montoPropuesta)}</div>
          <span className={[
            'inline-block mt-1 text-[10.5px] font-mono px-2 py-0.5 rounded-full border',
            propuesta.archivosGenerados
              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
              : 'bg-amber-100 text-amber-800 border-amber-200',
          ].join(' ')}
          >
            {propuesta.archivosGenerados ? 'Concluida' : (ESTADO_PROPUESTA_PAGO_LABEL[propuesta.estado]?.split(' —')[0] ?? propuesta.estado)}
          </span>
        </div>
      </div>

      {/* 5.1 — Introducir Parámetros de Pago en F110 */}
      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Introducir Parámetros de Pago en F110 (sociedad, vía de pago, fecha)" done={propuesta.parametrosIntroducidos} />
        {propuesta.pendienteParametros && (
          <div className="flex justify-end mt-3">
            <Button variant="secondary" loading={procesando} onClick={() => onIntroducirParametros(propuesta.id, PARAMETROS_PAGO_DEFAULT)}>
              <RuleTag>5.1</RuleTag>Introducir Parámetros de Pago
            </Button>
          </div>
        )}
        {propuesta.parametrosIntroducidos && (
          <EntryBox>
            Sociedad {propuesta.sociedad} · Vía de pago {propuesta.viaPago} · Fecha de pago {propuesta.fechaPago}
          </EntryBox>
        )}
      </div>

      {/* 5.1 (cont.) — Ejecutar Propuesta de Pago Automática */}
      {propuesta.parametrosIntroducidos && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Ejecutar Propuesta de Pago Automática (Sistema ERP)" done={propuesta.propuestaEjecutada} />
          {propuesta.pendienteEjecutarPropuesta && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onEjecutarPropuesta(propuesta.id)}>
                Ejecutar Propuesta de Pago Automática
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 5.2 — Revisar Reporte de Excepciones y Bloqueos */}
      {propuesta.propuestaEjecutada && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine
            label="¿Propuesta Correcta? — Revisar Reporte de Excepciones y Bloqueos"
            done={propuesta.propuestaCorrecta === true}
            mark={propuesta.propuestaCorrecta === false ? '✗' : undefined}
          />
          {propuesta.pendienteRevisarExcepciones && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onRevisarExcepciones(propuesta.id)}>
                <RuleTag>5.2</RuleTag>Revisar Reporte de Excepciones
              </Button>
            </div>
          )}
          {propuesta.propuestaCorrecta === false && (
            <>
              <Callout tone="warn">Con excepciones — un proveedor con datos bancarios desactualizados.</Callout>
              <div className="flex justify-end mt-3">
                <Button variant="secondary" loading={procesando} onClick={() => onAjustarReejecutar(propuesta.id)}>
                  Ajustar Parámetros y Reejecutar Propuesta
                </Button>
              </div>
            </>
          )}
          {propuesta.propuestaCorrecta === true && (
            <Callout tone="ok">Propuesta correcta — sin excepciones ni bloqueos pendientes.</Callout>
          )}
        </div>
      )}

      {/* 5.2 (cont.) — Aprobar Propuesta de Pago Final */}
      {propuesta.pendienteAprobar && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" loading={procesando} onClick={() => onAprobar(propuesta.id)}>
            Aprobar Propuesta de Pago Final
          </Button>
        </div>
      )}

      {/* 5.3 — Ejecutar Ejecución de Pago */}
      {propuesta.propuestaAprobada && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Ejecutar Ejecución de Pago (Sistema ERP)" done={propuesta.pagoEjecutado} />
          {propuesta.pendienteEjecutarPago && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onEjecutarPago(propuesta.id)}>
                <RuleTag>5.3</RuleTag>Ejecutar Ejecución de Pago
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 5.3 (cont.) — Generar Archivos Bancarios Planos (IDoc / N43) */}
      {propuesta.pagoEjecutado && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Generar Archivos Bancarios Planos (IDoc / N43)" done={propuesta.archivosGenerados} />
          {propuesta.pendienteGenerarArchivos && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onGenerarArchivos(propuesta.id)}>
                Generar Archivos Bancarios Planos
              </Button>
            </div>
          )}
          {propuesta.archivosGenerados && (
            <StepLine label="Archivos bancarios generados — habilita la Fase 06" done />
          )}
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Flujo de procesamiento automático y propuesta de pago (Fase 05)
// ─────────────────────────────────────────────────────────────────────────
function PropuestasPagoTab() {
  const {
    propuestas, loading, procesandoId, error,
    cargarPropuestas, iniciarPropuesta, introducirParametrosPago, ejecutarPropuestaAutomatica,
    revisarReporteExcepciones, ajustarParametrosYReejecutar, aprobarPropuestaFinal,
    ejecutarPagoPropuesta, generarArchivosBancarios,
  } = usePropuestasPago();

  const [lotesConciliados, setLotesConciliados] = useState([]);
  const [loadingLotes, setLoadingLotes] = useState(false);
  const [localError, setLocalError] = useState('');

  const cargarLotesConciliados = useCallback(async () => {
    setLoadingLotes(true);
    try {
      const data = await useCases.lotePago.getAll.execute();
      setLotesConciliados(data.filter((l) => l.pagosConciliadosGestion));
    } catch (err) {
      setLocalError(err.response?.data ?? err.message ?? 'Error al obtener los lotes conciliados en Fase 04');
    } finally {
      setLoadingLotes(false);
    }
  }, []);

  useEffect(() => {
    cargarPropuestas();
    cargarLotesConciliados();
  }, [cargarPropuestas, cargarLotesConciliados]);

  const lotesSinPropuesta = useMemo(
    () => lotesConciliados.filter(
      (l) => !propuestas.some((p) => p.lotePagoTesoreriaId === l.id),
    ),
    [lotesConciliados, propuestas],
  );

  async function handleIniciar(lotePagoTesoreriaId) {
    setLocalError('');
    const nueva = await iniciarPropuesta(lotePagoTesoreriaId);
    if (!nueva) setLocalError(error ?? 'No se pudo iniciar la propuesta de pago');
  }

  const todasConcluidas = propuestas.length > 0 && propuestas.every((p) => p.archivosGenerados);

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AP5-01 · F110</span>
        El sistema ejecuta la propuesta de pago automática sobre el lote aprobado; el Analista de AP revisa
        excepciones y bloqueos antes de aprobar la propuesta final y generar los archivos bancarios.
      </div>

      <ErrorBox error={localError || error} />

      {/* Sistema ERP — lotes conciliados en Fase 04 listos para iniciar la propuesta */}
      <div>
        <LaneNote>Sistema ERP</LaneNote>
        <Callout>
          Lotes conciliados en Fase 04 — {loadingLotes
            ? 'buscando lotes conciliados...'
            : `${lotesSinPropuesta.length} lote(s) pendiente(s) de iniciar la propuesta de pago automática.`}
        </Callout>

        {lotesSinPropuesta.length > 0 && (
          <div className="mt-3 flex flex-col gap-2">
            {lotesSinPropuesta.map((l) => (
              <div key={l.id} className="flex items-center justify-between gap-3 border border-slate-200 rounded-lg px-3.5 py-2.5 flex-wrap">
                <div className="text-sm">
                  <span className="font-mono font-semibold text-emerald-900">{l.numero}</span>
                  <span className="text-slate-500"> · Monto {fmt(l.montoLote)}</span>
                </div>
                <Button
                  variant="secondary"
                  loading={procesandoId === 'nuevo'}
                  onClick={() => handleIniciar(l.id)}
                >
                  Iniciar Propuesta de Pago
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Analista de Cuentas por Pagar — parametrización, revisión y aprobación de la propuesta */}
      <div>
        <LaneNote>Analista de Cuentas por Pagar</LaneNote>
        {loading ? (
          <div className="text-sm text-slate-400 py-6 text-center">Cargando propuestas de pago...</div>
        ) : propuestas.length === 0 ? (
          <div className="text-sm text-slate-400 py-6 text-center">
            Aún no hay propuestas de pago iniciadas. Inicia la propuesta a partir de un lote conciliado en Fase 04.
          </div>
        ) : (
          propuestas.map((p) => (
            <PropuestaCard
              key={p.id}
              propuesta={p}
              procesando={procesandoId === p.id}
              onIntroducirParametros={introducirParametrosPago}
              onEjecutarPropuesta={ejecutarPropuestaAutomatica}
              onRevisarExcepciones={revisarReporteExcepciones}
              onAjustarReejecutar={ajustarParametrosYReejecutar}
              onAprobar={aprobarPropuestaFinal}
              onEjecutarPago={ejecutarPagoPropuesta}
              onGenerarArchivos={generarArchivosBancarios}
            />
          ))
        )}
      </div>

      {propuestas.length > 0 && (
        todasConcluidas ? (
          <div>
            <LaneNote>Sistema ERP</LaneNote>
            <Callout tone="ok">
              <RuleTag>5.3</RuleTag>
              Archivos bancarios generados. Se libera la Fase 06 — Dispersión Bancaria y Conciliación de Cierre.
            </Callout>
            <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
              <button onClick={() => cargarPropuestas()} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                Actualizar panel
              </button>
              <div className="flex flex-col items-end gap-1">
                <Button onClick={() => navigate('/fico/fi-ap/dispersion-bancaria')}>Continuar a Fase 06 — Dispersión Bancaria →</Button>
              </div>
            </div>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Completa el flujo de cada propuesta para continuar el ciclo.</p>
        )
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de propuestas de pago
// ─────────────────────────────────────────────────────────────────────────
function HistorialPropuestasTab() {
  const [propuestas, setPropuestas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Propuesta' },
    { key: 'numeroLotePago', label: 'Lote (Fase 04)' },
    { key: 'montoPropuesta', label: 'Monto', render: (val) => fmt(val) },
    { key: 'intentos', label: 'Intentos' },
    {
      key: 'propuestaCorrecta',
      label: 'Revisión',
      render: (val) => (val === null ? 'Pendiente' : (val ? 'Correcta' : 'Con excepciones')),
    },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_PROPUESTA_PAGO_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.propuestaPago.getAll.execute();
      setPropuestas(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las propuestas de pago');
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
        <p className="text-sm text-slate-500">{propuestas.length} propuestas de pago registradas</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando propuestas de pago...</div>
        ) : (
          <Table columns={columns} data={propuestas} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 05: Procesamiento Automático y Propuesta de Pago
// ─────────────────────────────────────────────────────────────────────────
export default function PropuestaPagoPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiApRail activeStep={4} maxReached={4} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-AP5-01 · F110"
          title="Procesamiento Automático y Propuesta de Pago"
          description="El sistema ejecuta la propuesta de pago automática sobre el lote aprobado; el Analista de AP revisa excepciones y bloqueos antes de aprobar la propuesta final y generar los archivos bancarios."
          badge="FI-AP"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Propuesta de pago' },
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

        {tab === 'flujo' ? <PropuestasPagoTab /> : <HistorialPropuestasTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 05 de {CICLO_FI_AP_FASES.length} — la generación de archivos bancarios habilita la Fase 06 (Dispersión Bancaria y Conciliación de Cierre)
      </p>
    </div>
  );
}
