import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Select   from '@/presentation/components/ui/Select';
import Table    from '@/presentation/components/ui/Table';
import FiApRail, { CICLO_FI_AP_FASES } from '@/presentation/components/fiAp/FiApRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useExcepcionesFacturacion } from '@/presentation/hooks/useExcepcionesFacturacion';
import { useCases } from '@/infrastructure';
import {
  ESTADO_EXCEPCION_FACTURACION_LABEL,
  TIPO_DISCREPANCIA,
  TIPO_DISCREPANCIA_LABEL,
} from '@/domain/models/ExcepcionFacturacion';

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
// Tarjeta de una excepción de facturación (recipe-card del prototipo)
// ─────────────────────────────────────────────────────────────────────────
function ExcepcionCard({ excepcion, procesando, onRevisar, onClasificar }) {
  const [tipo, setTipo] = useState('');

  const tipoOptions = [
    { value: TIPO_DISCREPANCIA.PRECIO, label: TIPO_DISCREPANCIA_LABEL[TIPO_DISCREPANCIA.PRECIO] },
    { value: TIPO_DISCREPANCIA.CANTIDAD, label: TIPO_DISCREPANCIA_LABEL[TIPO_DISCREPANCIA.CANTIDAD] },
  ];

  return (
    <div className="border border-slate-200 rounded-xl px-4 py-3.5 mb-3 bg-white">
      <div className="flex justify-between items-center gap-3 flex-wrap">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-900">{excepcion.numero}</span>
          <span className="text-slate-500 text-sm"> · {excepcion.razonSocialProveedor}</span>
          <div className="text-xs text-slate-500 mt-0.5">
            Factura {excepcion.numeroFactura} — Monto factura {fmt(excepcion.montoFactura)} · Diferencia {fmt(excepcion.diferencia)}
          </div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(excepcion.montoFactura)}</div>
          {excepcion.clasificada ? (
            <span className="inline-block mt-1 text-[10.5px] font-mono px-2 py-0.5 rounded-full bg-amber-100 text-amber-800 border border-amber-200">
              Discrepancia: {TIPO_DISCREPANCIA_LABEL[excepcion.tipoDiscrepancia]?.split(' —')[0] ?? excepcion.tipoDiscrepancia}
            </span>
          ) : (
            <span className="inline-block mt-1"><Badge value={undefined} /></span>
          )}
        </div>
      </div>

      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Factura bloqueada por el sistema" done />
        <StepLine label="1.1 Revisar Panel de Facturas Bloqueadas" done={excepcion.revisada} />
        {excepcion.revisada && (
          <StepLine
            label={excepcion.clasificada
              ? `1.2 Discrepancia clasificada como: ${TIPO_DISCREPANCIA_LABEL[excepcion.tipoDiscrepancia]?.split(' —')[0] ?? excepcion.tipoDiscrepancia}`
              : '1.2 Analizar Discrepancia (Precio Vademécum o Cantidad Recibida)'}
            done={excepcion.clasificada}
          />
        )}
        {excepcion.clasificada && (
          <StepLine label="1.3 Disparar Notificación Interna a Compras" done />
        )}
      </div>

      {excepcion.pendienteRevision && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" onClick={() => onRevisar(excepcion.id)} loading={procesando}>
            Revisar Factura Bloqueada
          </Button>
        </div>
      )}

      {excepcion.pendienteClasificacion && (
        <div className="flex items-end gap-3 justify-end mt-3 flex-wrap">
          <div className="w-64">
            <Select
              label="Tipo de discrepancia"
              name={`tipo-${excepcion.id}`}
              value={tipo}
              onChange={(e) => setTipo(e.target.value)}
              options={tipoOptions}
              placeholder="Selecciona el tipo..."
            />
          </div>
          <Button
            variant="secondary"
            disabled={!tipo}
            loading={procesando}
            onClick={() => onClasificar(excepcion.id, tipo)}
          >
            Analizar y Clasificar Discrepancia
          </Button>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Flujo de captura de excepciones (Fase 01)
// ─────────────────────────────────────────────────────────────────────────
function CapturaExcepcionesTab({ navigate, onMaxReachedChange }) {
  const {
    excepciones, loading, procesandoId, error,
    cargarExcepciones, capturarExcepcion, revisarExcepcion, clasificarExcepcion,
  } = useExcepcionesFacturacion();

  const [conciliacionesBloqueadas, setConciliacionesBloqueadas] = useState([]);
  const [loadingConciliaciones, setLoadingConciliaciones] = useState(false);
  const [localError, setLocalError] = useState('');

  const cargarConciliacionesBloqueadas = useCallback(async () => {
    setLoadingConciliaciones(true);
    try {
      const data = await useCases.conciliacion.getAll.execute({});
      setConciliacionesBloqueadas(data.filter((c) => c.bloqueada));
    } catch (err) {
      setLocalError(err.response?.data ?? err.message ?? 'Error al obtener las conciliaciones bloqueadas (MRBR)');
    } finally {
      setLoadingConciliaciones(false);
    }
  }, []);

  useEffect(() => {
    cargarExcepciones();
    cargarConciliacionesBloqueadas();
  }, [cargarExcepciones, cargarConciliacionesBloqueadas]);

  const conciliacionesSinCapturar = useMemo(
    () => conciliacionesBloqueadas.filter(
      (c) => !excepciones.some((e) => e.conciliacionTresViasId === c.id),
    ),
    [conciliacionesBloqueadas, excepciones],
  );

  async function handleCapturar(conciliacionTresViasId) {
    setLocalError('');
    const nueva = await capturarExcepcion({ conciliacionTresViasId });
    if (!nueva) setLocalError(error ?? 'No se pudo capturar la excepción de facturación');
  }

  const todasClasificadas = excepciones.length > 0 && excepciones.every((e) => e.clasificada);

  useEffect(() => {
    if (todasClasificadas) onMaxReachedChange?.(1);
  }, [todasClasificadas, onMaxReachedChange]);

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AP1-01 · Frontera Logística</span>
        El sistema bloquea automáticamente toda factura de proveedor cuya desviación frente al vademécum de precios o
        a la cantidad recibida exceda la tolerancia permitida. Cada excepción se clasifica antes de derivarla a Compras.
      </div>

      <ErrorBox error={localError || error} />

      {/* Sistema ERP — Inconsistencia Automatizada Detectada */}
      <div>
        <LaneNote>Sistema ERP</LaneNote>
        <Callout>
          Inconsistencia Automatizada Detectada — {loadingConciliaciones
            ? 'buscando conciliaciones bloqueadas...'
            : `${conciliacionesSinCapturar.length} conciliación(es) de 3 vías bloqueada(s) en MRBR pendiente(s) de capturar como excepción de facturación.`}
        </Callout>

        {conciliacionesSinCapturar.length > 0 && (
          <div className="mt-3 flex flex-col gap-2">
            {conciliacionesSinCapturar.map((c) => (
              <div key={c.id} className="flex items-center justify-between gap-3 border border-slate-200 rounded-lg px-3.5 py-2.5 flex-wrap">
                <div className="text-sm">
                  <span className="font-mono font-semibold text-emerald-900">{c.numero}</span>
                  <span className="text-slate-500"> · OC {c.numeroOrdenCompra} · {c.razonSocialProveedor}</span>
                </div>
                <Button
                  variant="secondary"
                  loading={procesandoId === 'nueva'}
                  onClick={() => handleCapturar(c.id)}
                >
                  Capturar Excepción de Facturación
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Analista de Cuentas por Pagar — Panel de facturas bloqueadas */}
      <div>
        <LaneNote>Analista de Cuentas por Pagar</LaneNote>
        {loading ? (
          <div className="text-sm text-slate-400 py-6 text-center">Cargando panel de facturas bloqueadas...</div>
        ) : excepciones.length === 0 ? (
          <div className="text-sm text-slate-400 py-6 text-center">
            Aún no hay excepciones capturadas. Captura una conciliación bloqueada para iniciar el flujo.
          </div>
        ) : (
          excepciones.map((exc) => (
            <ExcepcionCard
              key={exc.id}
              excepcion={exc}
              procesando={procesandoId === exc.id}
              onRevisar={revisarExcepcion}
              onClasificar={clasificarExcepcion}
            />
          ))
        )}
      </div>

      {excepciones.length > 0 && (
        todasClasificadas ? (
          <div>
            <LaneNote>Sistema ERP</LaneNote>
            <Callout tone="ok">
              <RuleTag>1.3</RuleTag>
              Notificación interna disparada a Compras / Category Manager con el detalle de cada discrepancia.
            </Callout>
            <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
              <button onClick={cargarExcepciones} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                Actualizar panel
              </button>
              <Button onClick={() => navigate(CICLO_FI_AP_FASES[1].path)}>
                Continuar a Fase 02 — Gestión de Disputas Comerciales →
              </Button>
            </div>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Revisa y clasifica cada factura bloqueada para continuar el ciclo.</p>
        )
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de excepciones de facturación
// ─────────────────────────────────────────────────────────────────────────
function HistorialExcepcionesTab() {
  const [excepciones, setExcepciones] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Excepción' },
    { key: 'numeroFactura', label: 'Factura (MIRO)' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'montoFactura', label: 'Monto factura', render: (val) => fmt(val) },
    { key: 'diferencia', label: 'Diferencia', render: (val) => fmt(val) },
    { key: 'tipoDiscrepancia', label: 'Discrepancia', render: (val) => val ?? '— pendiente' },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_EXCEPCION_FACTURACION_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.excepcionFacturacion.getAll.execute({});
      setExcepciones(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las excepciones de facturación');
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
        <p className="text-sm text-slate-500">{excepciones.length} excepciones de facturación registradas</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando excepciones de facturación...</div>
        ) : (
          <Table columns={columns} data={excepciones} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 01: Captura de Excepciones de Facturación (Frontera Logística)
// ─────────────────────────────────────────────────────────────────────────
export default function ExcepcionFacturacionPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');
  const [maxReached, setMaxReached] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiApRail activeStep={0} maxReached={maxReached} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-AP1-01 · Frontera Logística"
          title="Captura de Excepciones de Facturación"
          description="El sistema bloquea automáticamente toda factura de proveedor cuya desviación frente al vademécum de precios o a la cantidad recibida exceda la tolerancia permitida. Cada excepción se clasifica antes de derivarla a Compras."
          badge="FI-AP"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Captura de excepciones' },
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

        {tab === 'flujo'
          ? <CapturaExcepcionesTab navigate={navigate} onMaxReachedChange={setMaxReached} />
          : <HistorialExcepcionesTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 01 de {CICLO_FI_AP_FASES.length} — la notificación interna a Compras habilita la Fase 02 (Gestión Humana de Disputas Comerciales)
      </p>
    </div>
  );
}
