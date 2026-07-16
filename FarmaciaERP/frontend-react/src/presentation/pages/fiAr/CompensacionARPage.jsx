import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Select   from '@/presentation/components/ui/Select';
import FiArRail, { CICLO_FI_AR_FASES } from '@/presentation/components/fiAr/FiArRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useCompensacionAR } from '@/presentation/hooks/useCompensacionAR';
import { useCases } from '@/infrastructure';
import { ESTADO_COMPENSACION_AR, ESTADO_COMPENSACION_AR_LABEL } from '@/domain/models/CompensacionAR';

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

function EstadoBadge({ estado }) {
  const styles = {
    [ESTADO_COMPENSACION_AR.COMPENSADO]: 'bg-slate-100 text-slate-600 border border-slate-200',
    [ESTADO_COMPENSACION_AR.REPORTE_GENERADO]: 'bg-amber-50 text-amber-700 border border-amber-200',
    [ESTADO_COMPENSACION_AR.SALDO_CONFIRMADO]: 'bg-amber-50 text-amber-700 border border-amber-200',
    [ESTADO_COMPENSACION_AR.CERRADO]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
  };
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[estado] ?? 'bg-slate-100 text-slate-600'}`}>
      {ESTADO_COMPENSACION_AR_LABEL[estado] ?? estado}
    </span>
  );
}

function StepLine({ label, done }) {
  return (
    <div className={`text-[12.6px] py-0.5 ${done ? 'text-emerald-700' : 'text-slate-400'}`}>
      {done ? '✓' : '○'} {label}
    </div>
  );
}

function Kpi({ label, value, highlight }) {
  return (
    <div className={`rounded-xl border px-4 py-3 flex flex-col gap-1 ${highlight ? 'bg-emerald-50 border-emerald-200' : 'bg-slate-50 border-slate-200'}`}>
      <span className="text-[11px] uppercase tracking-wide text-slate-500">{label}</span>
      <span className={`font-mono font-semibold text-lg ${highlight ? 'text-emerald-700' : 'text-slate-800'}`}>{value}</span>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Tarjeta de la compensación final con sus pasos de cierre
// ─────────────────────────────────────────────────────────────────────────
function CompensacionCard({ compensacion, procesando, onGenerarReporte, onConfirmarSaldo, onCerrarIngresos }) {
  return (
    <div className="border border-slate-200 rounded-xl p-4 bg-white flex flex-col gap-4">
      <div className="flex justify-between items-start flex-wrap gap-2">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-800">Compensación #{compensacion.id}</span>
          <div className="text-xs text-slate-400 mt-0.5">Cierre del ciclo FI-AR del período</div>
        </div>
        <EstadoBadge estado={compensacion.estado} />
      </div>

      <div className="pt-1 border-t border-dashed border-slate-200">
        <StepLine label="Compensación automática aplicada sobre la cuenta del cliente" done={compensacion.compensado} />
        <StepLine label="Reporte de Rendimiento Comercial y Margen de la Cadena generado" done={compensacion.reporteGenerado} />
        <StepLine label="Saldo limpio confirmado en cuentas corrientes" done={compensacion.saldoConfirmado} />
        <StepLine label="Cierre de Ingresos por Convenio completado" done={compensacion.cerrado} />
      </div>

      {compensacion.reporteGenerado && (
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          <Kpi label="Ventas del período (POS)" value={fmt(compensacion.montoVentas)} />
          <Kpi label="Recetas aprobadas/liberadas" value={fmt(compensacion.montoAprobadas)} />
          <Kpi label="Pérdidas por ajustes técnicos" value={fmt(compensacion.perdidas)} />
          <Kpi label={`Margen neto (${compensacion.margenPct}%)`} value={fmt(compensacion.margenNeto)} highlight />
        </div>
      )}

      {compensacion.requiereGenerarReporte && (
        <div className="flex justify-end">
          <Button className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={onGenerarReporte}>
            Generar Reporte de Rendimiento Comercial
          </Button>
        </div>
      )}

      {compensacion.requiereConfirmarSaldo && (
        <div className="flex justify-end">
          <Button variant="secondary" className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={onConfirmarSaldo}>
            Confirmar Saldo Limpio en Cuentas Corrientes
          </Button>
        </div>
      )}

      {compensacion.requiereCerrarIngresos && (
        <div className="flex justify-end">
          <Button className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={onCerrarIngresos}>
            Cerrar Ingresos por Convenio
          </Button>
        </div>
      )}

      {compensacion.cerrado && (
        <div className="px-4 py-3 bg-emerald-50 border border-emerald-200 rounded-xl text-sm text-emerald-700">
          ✓ Ciclo FI-AR del período finalizado exitosamente para este lote.
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Selección del lote (Fase 02) para cerrar su ciclo FI-AR
// ─────────────────────────────────────────────────────────────────────────
function SeleccionLotePanel({ loteIdInicial, onSeleccionar }) {
  const [lotes, setLotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loteId, setLoteId] = useState(loteIdInicial ?? '');

  useEffect(() => {
    async function cargar() {
      setLoading(true);
      const todas = await useCases.contabilizacionAR.getAll.execute({});
      setLotes(todas.filter((c) => c.consolidado));
      setLoading(false);
    }
    cargar();
  }, []);

  const loteOptions = lotes.map((l) => ({ value: l.id, label: `${l.cierreCajaNumero} — lote consolidado` }));

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AR6-01 · SAP FI-AR</span>
        Tesorería aplica la compensación automática sobre la cuenta del cliente, se genera el Reporte de
        Rendimiento Comercial y Margen de la Cadena, se confirma el saldo limpio en cuentas corrientes y se
        cierran los ingresos por convenio — cerrando el ciclo FI-AR del período.
      </div>

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">
          Lote consolidado (Fase 02)
        </p>
        <Select
          name="loteId"
          placeholder={loading ? 'Cargando lotes...' : 'Selecciona un lote consolidado...'}
          value={loteId}
          onChange={(e) => setLoteId(e.target.value)}
          options={loteOptions}
          disabled={loading}
        />
        {!loading && loteOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay lotes consolidados disponibles. Vuelve a la Fase 02 para consolidar un lote.
          </p>
        )}
      </div>

      <div className="flex justify-end">
        <Button onClick={() => onSeleccionar(loteId)} disabled={!loteId}>
          Cerrar Ciclo FI-AR del Lote →
        </Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Flujo principal — compensación final del lote seleccionado
// ─────────────────────────────────────────────────────────────────────────
function CompensacionFinalFlow({ loteId, onCambiarLote }) {
  const {
    compensacion, loading, procesando, error,
    cargarCompensacion, aplicarCompensacion, generarReporte, confirmarSaldo, cerrarIngresos,
  } = useCompensacionAR(loteId);

  useEffect(() => {
    cargarCompensacion();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loteId]);

  return (
    <div className="p-6 flex flex-col gap-5">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AR6-01 · SAP FI-AR</span>
        Cierre del ciclo FI-AR del período: compensación automática, reporte de rendimiento, confirmación de
        saldo y cierre de ingresos por convenio.
      </div>

      <ErrorBox error={error} />

      {loading ? (
        <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando compensación del lote...</div>
      ) : !compensacion ? (
        <div className="flex flex-col gap-3">
          <p className="text-sm text-slate-500">
            Este lote todavía no tiene compensación final. Aplica la compensación automática sobre la cuenta
            del cliente para iniciar el cierre del ciclo FI-AR.
          </p>
          <div className="flex justify-end">
            <Button loading={procesando} onClick={aplicarCompensacion}>
              Aplicar Compensación Automática
            </Button>
          </div>
        </div>
      ) : (
        <CompensacionCard
          compensacion={compensacion}
          procesando={procesando}
          onGenerarReporte={generarReporte}
          onConfirmarSaldo={confirmarSaldo}
          onCerrarIngresos={cerrarIngresos}
        />
      )}

      <div className="flex justify-between items-center flex-wrap gap-3 pt-2">
        <button onClick={onCambiarLote} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
          Cerrar otro lote
        </button>
        {compensacion?.finalizado ? (
          <span className="font-mono text-[11px] px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200">
            Ciclo FI-AR finalizado ✓
          </span>
        ) : (
          <p className="text-xs text-slate-400">Completa cada paso para finalizar el ciclo FI-AR del lote.</p>
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 06: Compensación Final y Análisis de Margen Neto
// ─────────────────────────────────────────────────────────────────────────
export default function CompensacionARPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const contabilizacionARIdParam = searchParams.get('contabilizacionARId');

  const [loteId, setLoteId] = useState(contabilizacionARIdParam ?? null);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiArRail activeStep={5} maxReached={5} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: AR6-01 · SAP FI-AR"
          title="Compensación Final y Análisis de Margen Neto"
          description="Tesorería aplica la compensación automática sobre la cuenta del cliente, se genera el Reporte de Rendimiento Comercial y Margen de la Cadena, se confirma el saldo limpio en cuentas corrientes y se cierran los ingresos por convenio."
          badge="FI-AR"
        />

        {loteId ? (
          <CompensacionFinalFlow loteId={loteId} onCambiarLote={() => setLoteId(null)} />
        ) : (
          <SeleccionLotePanel loteIdInicial={contabilizacionARIdParam} onSeleccionar={setLoteId} />
        )}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 06 de {CICLO_FI_AR_FASES.length} — el cierre de ingresos por convenio finaliza el ciclo FI-AR del período para este lote
      </p>
    </div>
  );
}
