import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Table    from '@/presentation/components/ui/Table';
import FiApRail, { CICLO_FI_AP_FASES } from '@/presentation/components/fiAp/FiApRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useDisputasComerciales } from '@/presentation/hooks/useDisputasComerciales';
import { useCases } from '@/infrastructure';
import { ESTADO_DISPUTA_COMERCIAL_LABEL } from '@/domain/models/DisputaComercial';

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

function StepLine({ label, done }) {
  return (
    <div className={`text-[12.6px] py-0.5 ${done ? 'text-emerald-700' : 'text-slate-400'}`}>
      {done ? '✓' : '○'} {label}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Tarjeta de negociación de una disputa comercial (recipe-card del prototipo)
// ─────────────────────────────────────────────────────────────────────────
function DisputaCard({
  disputa, procesando,
  onCotejar, onCuantificar, onValidar, onAbrirNegociacion,
  onContraoferta, onAceptarAbsorcion, onReabrirNegociacion, onResolver,
}) {
  const [monto, setMonto] = useState('');

  const submitContraoferta = () => {
    if (!monto || Number(monto) <= 0) return;
    onContraoferta(disputa.id, Number(monto));
    setMonto('');
  };

  return (
    <div className="border border-slate-200 rounded-xl px-4 py-3.5 mb-3 bg-white">
      <div className="flex justify-between items-center gap-3 flex-wrap">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-900">{disputa.numero}</span>
          <span className="text-slate-500 text-sm"> · {disputa.razonSocialProveedor}</span>
          <div className="text-xs text-slate-500 mt-0.5">
            Excepción {disputa.numeroExcepcion} — Discrepancia de {disputa.tipoDiscrepancia?.toLowerCase?.() ?? disputa.tipoDiscrepancia}
            {disputa.cuantificada && <> · {fmt(disputa.impactoFinanciero)}</>}
          </div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(disputa.impactoFinanciero)}</div>
          <span className={[
            'inline-block mt-1 text-[10.5px] font-mono px-2 py-0.5 rounded-full border',
            disputa.resueltaWorkflow
              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
              : 'bg-amber-100 text-amber-800 border-amber-200',
          ].join(' ')}
          >
            {disputa.resueltaWorkflow ? 'Resuelta' : `Ronda ${disputa.rondaNegociacion}`}
          </span>
        </div>
      </div>

      {/* Pasos 2.1.x */}
      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Notificación de discrepancia recibida (Sistema ERP Central)" done />
        <StepLine label="2.1.1 Extracción y Cotejo de Datos de Facturación vs. Acuerdos" done={disputa.cotejada} />
        {disputa.cotejada && (
          <StepLine label="2.1.2 Cuantificación del Impacto Financiero de la Desviación" done={disputa.cuantificada} />
        )}
        {disputa.cuantificada && (
          <StepLine
            label={`Impacto financiero cuantificado: ${fmt(disputa.impactoFinanciero)}`}
            done
          />
        )}
        {disputa.cuantificada && (
          <StepLine label="2.1.3 Revisión y Validación de la Desviación" done={disputa.validadaDesviacion} />
        )}
      </div>

      {/* Pasos 2.1.x — acciones */}
      {disputa.pendienteCotejo && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" loading={procesando} onClick={() => onCotejar(disputa.id)}>
            Cotejar Factura vs. Contrato
          </Button>
        </div>
      )}
      {disputa.pendienteCuantificacion && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" loading={procesando} onClick={() => onCuantificar(disputa.id)}>
            Cuantificar Impacto Financiero
          </Button>
        </div>
      )}
      {disputa.pendienteValidacion && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" loading={procesando} onClick={() => onValidar(disputa.id)}>
            Validar Desviación
          </Button>
        </div>
      )}

      {/* Negociación 2.2.x */}
      {disputa.validadaDesviacion && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine
            label={`2.2.1 Apertura de Disputa con Ejecutivo de Droguería${disputa.rondaNegociacion > 0 ? ` (ronda ${disputa.rondaNegociacion})` : ''}`}
            done={disputa.disputaAbierta || disputa.resueltaWorkflow}
          />
          {disputa.montoContraoferta > 0 && (
            <StepLine
              label={`${disputa.razonSocialProveedor} envió contrapropuesta: absorbe ${fmt(disputa.montoContraoferta)} de ${fmt(disputa.impactoFinanciero)}`}
              done
            />
          )}
          {disputa.montoContraoferta > 0 && (
            <StepLine label="2.2.2 Evaluación de Contrapropuesta del Laboratorio" done={disputa.absorbeAceptado !== null} />
          )}
          {disputa.absorbeAceptado === true && (
            <StepLine label={`¿Se absorbe la diferencia comercial? Sí — absorbe ${fmt(disputa.montoContraoferta)}`} done />
          )}
          {disputa.resueltaWorkflow && (
            <StepLine label="Fase 2 AP concluida — apta para Fase 3" done />
          )}
        </div>
      )}

      {disputa.puedeAbrirNegociacion && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" loading={procesando} onClick={() => onAbrirNegociacion(disputa.id)}>
            Abrir Disputa con Ejecutivo (Ronda {disputa.rondaNegociacion + 1})
          </Button>
        </div>
      )}

      {disputa.esperandoContraoferta && (
        <div className="flex items-end gap-3 justify-end mt-3 flex-wrap">
          <div className="w-48">
            <Input
              label="Contrapropuesta (S/)"
              name={`contraoferta-${disputa.id}`}
              type="number"
              value={monto}
              onChange={(e) => setMonto(e.target.value)}
              placeholder="0.00"
            />
          </div>
          <Button variant="secondary" loading={procesando} disabled={!monto} onClick={submitContraoferta}>
            {disputa.razonSocialProveedor}: Enviar Contrapropuesta
          </Button>
        </div>
      )}

      {disputa.pendienteEvaluacionAbsorcion && (
        <div className="flex justify-end gap-3 mt-3 flex-wrap">
          <Button variant="secondary" loading={procesando} onClick={() => onAceptarAbsorcion(disputa.id)}>
            ¿Se Absorbe la Diferencia Comercial? — Sí
          </Button>
          <Button variant="outline" loading={procesando} onClick={() => onReabrirNegociacion(disputa.id)}>
            No, reabrir negociación
          </Button>
        </div>
      )}

      {disputa.puedeResolverWorkflow && (
        <div className="flex justify-end mt-3">
          <Button loading={procesando} onClick={() => onResolver(disputa.id)}>
            2.3.1 Registrar Resolución en Workflow del ERP
          </Button>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Flujo de negociación de disputas comerciales (Fase 02)
// ─────────────────────────────────────────────────────────────────────────
function DisputasComercialesTab({ onMaxReachedChange }) {
  const {
    disputas, loading, procesandoId, error,
    cargarDisputas, abrirDisputa, cotejar, cuantificar, validarDesviacion,
    abrirNegociacion, registrarContraoferta, aceptarAbsorcion, reabrirNegociacion, resolverWorkflow,
  } = useDisputasComerciales();

  const [excepcionesNotificadas, setExcepcionesNotificadas] = useState([]);
  const [loadingExcepciones, setLoadingExcepciones] = useState(false);
  const [localError, setLocalError] = useState('');

  const cargarExcepcionesNotificadas = useCallback(async () => {
    setLoadingExcepciones(true);
    try {
      const data = await useCases.excepcionFacturacion.getAll.execute({});
      setExcepcionesNotificadas(data.filter((e) => e.notificada));
    } catch (err) {
      setLocalError(err.response?.data ?? err.message ?? 'Error al obtener las excepciones notificadas a Compras');
    } finally {
      setLoadingExcepciones(false);
    }
  }, []);

  useEffect(() => {
    cargarDisputas();
    cargarExcepcionesNotificadas();
  }, [cargarDisputas, cargarExcepcionesNotificadas]);

  const excepcionesSinDisputa = useMemo(
    () => excepcionesNotificadas.filter(
      (e) => !disputas.some((d) => d.excepcionFacturacionId === e.id),
    ),
    [excepcionesNotificadas, disputas],
  );

  async function handleAbrir(excepcionFacturacionId) {
    setLocalError('');
    const nueva = await abrirDisputa(excepcionFacturacionId);
    if (!nueva) setLocalError(error ?? 'No se pudo abrir la disputa comercial');
  }

  const todasResueltas = disputas.length > 0 && disputas.every((d) => d.resueltaWorkflow);

  useEffect(() => {
    if (todasResueltas) onMaxReachedChange?.(2);
  }, [todasResueltas, onMaxReachedChange]);

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AP2-01 · Workflow ERP</span>
        El Comprador / Category Manager coteja la factura contra los acuerdos comerciales, cuantifica el impacto
        financiero y negocia con el laboratorio o droguería hasta acordar si la diferencia se absorbe.
      </div>

      <ErrorBox error={localError || error} />

      {/* Sistema ERP — Notificación de discrepancia recibida */}
      <div>
        <LaneNote>Sistema ERP</LaneNote>
        <Callout>
          Notificación de discrepancia recibida — {loadingExcepciones
            ? 'buscando excepciones notificadas a Compras...'
            : `${excepcionesSinDisputa.length} excepción(es) de facturación notificada(s) pendiente(s) de abrir disputa comercial.`}
        </Callout>

        {excepcionesSinDisputa.length > 0 && (
          <div className="mt-3 flex flex-col gap-2">
            {excepcionesSinDisputa.map((e) => (
              <div key={e.id} className="flex items-center justify-between gap-3 border border-slate-200 rounded-lg px-3.5 py-2.5 flex-wrap">
                <div className="text-sm">
                  <span className="font-mono font-semibold text-emerald-900">{e.numero}</span>
                  <span className="text-slate-500"> · Factura {e.numeroFactura} · {e.razonSocialProveedor}</span>
                </div>
                <Button
                  variant="secondary"
                  loading={procesandoId === 'nueva'}
                  onClick={() => handleAbrir(e.id)}
                >
                  Abrir Disputa Comercial
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Comprador / Category Manager — negociación */}
      <div>
        <LaneNote>Comprador / Category Manager</LaneNote>
        {loading ? (
          <div className="text-sm text-slate-400 py-6 text-center">Cargando disputas comerciales...</div>
        ) : disputas.length === 0 ? (
          <div className="text-sm text-slate-400 py-6 text-center">
            Aún no hay disputas comerciales abiertas. Abre una desde una excepción notificada para iniciar la negociación.
          </div>
        ) : (
          disputas.map((d) => (
            <DisputaCard
              key={d.id}
              disputa={d}
              procesando={procesandoId === d.id}
              onCotejar={cotejar}
              onCuantificar={cuantificar}
              onValidar={validarDesviacion}
              onAbrirNegociacion={abrirNegociacion}
              onContraoferta={registrarContraoferta}
              onAceptarAbsorcion={aceptarAbsorcion}
              onReabrirNegociacion={reabrirNegociacion}
              onResolver={resolverWorkflow}
            />
          ))
        )}
      </div>

      {disputas.length > 0 && (
        todasResueltas ? (
          <div>
            <LaneNote>Sistema ERP</LaneNote>
            <Callout tone="ok">
              <RuleTag>2.3</RuleTag>
              Disputas comerciales resueltas en el workflow del ERP. Se libera la Fase 03 — Ajustes Contables y Regularización.
            </Callout>
            <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
              <button onClick={() => cargarDisputas()} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                Actualizar panel
              </button>
              <Button onClick={() => navigate(CICLO_FI_AP_FASES[2].path)}>
                Continuar a Fase 03 — Ajustes Contables y Regularización →
              </Button>
            </div>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Negocia y resuelve cada disputa comercial para continuar el ciclo.</p>
        )
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de disputas comerciales
// ─────────────────────────────────────────────────────────────────────────
function HistorialDisputasTab() {
  const [disputas, setDisputas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Disputa' },
    { key: 'numeroExcepcion', label: 'Excepción (Fase 01)' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'impactoFinanciero', label: 'Impacto financiero', render: (val) => fmt(val) },
    { key: 'rondaNegociacion', label: 'Rondas' },
    { key: 'montoContraoferta', label: 'Última contraoferta', render: (val) => (val ? fmt(val) : '—') },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_DISPUTA_COMERCIAL_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.disputaComercial.getAll.execute({});
      setDisputas(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las disputas comerciales');
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
        <p className="text-sm text-slate-500">{disputas.length} disputas comerciales registradas</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando disputas comerciales...</div>
        ) : (
          <Table columns={columns} data={disputas} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 02: Gestión Humana de Disputas Comerciales (Workflow ERP)
// ─────────────────────────────────────────────────────────────────────────
export default function DisputaComercialPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');
  const [maxReached, setMaxReached] = useState(1);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiApRail activeStep={1} maxReached={maxReached} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-AP2-01 · Workflow ERP"
          title="Gestión Humana de Disputas Comerciales"
          description="El Comprador / Category Manager coteja la factura contra los acuerdos comerciales, cuantifica el impacto financiero y negocia con el laboratorio o droguería hasta acordar si la diferencia se absorbe."
          badge="FI-AP"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Negociación de disputas' },
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

        {tab === 'flujo' ? <DisputasComercialesTab onMaxReachedChange={setMaxReached} /> : <HistorialDisputasTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 02 de {CICLO_FI_AP_FASES.length} — la resolución en el workflow del ERP habilita la Fase 03 (Ajustes Contables y Regularización)
      </p>
    </div>
  );
}
