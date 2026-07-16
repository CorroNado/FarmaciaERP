import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import FiArRail, { CICLO_FI_AR_FASES } from '@/presentation/components/fiAr/FiArRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useCobroAR } from '@/presentation/hooks/useCobroAR';
import { useCases } from '@/infrastructure';
import { ESTADO_COBRO_AR, ESTADO_COBRO_AR_LABEL } from '@/domain/models/CobroAR';

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
    [ESTADO_COBRO_AR.INTERPRETADO]: 'bg-slate-100 text-slate-600 border border-slate-200',
    [ESTADO_COBRO_AR.CONCILIADO]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
    [ESTADO_COBRO_AR.DESCUADRADO]: 'bg-red-50 text-red-600 border border-red-200',
    [ESTADO_COBRO_AR.AJUSTADO]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
    [ESTADO_COBRO_AR.REGISTRADO]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
  };
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[estado] ?? 'bg-slate-100 text-slate-600'}`}>
      {ESTADO_COBRO_AR_LABEL[estado] ?? estado}
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

// ─────────────────────────────────────────────────────────────────────────
// 5.1 — Formulario de interpretación del archivo de transferencia bancaria
// ─────────────────────────────────────────────────────────────────────────
function InterpretarArchivoForm({ onInterpretar, procesando }) {
  const [retenciones, setRetenciones] = useState('');
  const [localError, setLocalError] = useState('');

  async function handleSubmit() {
    setLocalError('');
    if (retenciones === '' || Number(retenciones) < 0) {
      setLocalError('Ingresa el monto de retenciones aplicadas por la entidad recaudadora');
      return;
    }
    const ok = await onInterpretar(retenciones);
    if (!ok) setLocalError('No se pudo interpretar el archivo de transferencia');
  }

  return (
    <div className="border border-slate-200 rounded-xl p-4 bg-slate-50 flex flex-col gap-3">
      <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide">
        5.1 · Interpretar Archivo de Transferencia Bancaria
      </p>
      <p className="text-xs text-slate-500">
        El monto transferido se calcula a partir del monto esperado del lote (cobertura de la aseguradora, o
        recetas aprobadas/liberadas más el copago) menos las retenciones aplicadas por la entidad recaudadora.
      </p>
      <ErrorBox error={localError} />
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
        <Input
          label="Retenciones aplicadas"
          name="retenciones"
          type="number"
          value={retenciones}
          onChange={(e) => setRetenciones(e.target.value)}
          placeholder="S/ 0.00"
        />
      </div>
      <div className="flex justify-end">
        <Button loading={procesando} onClick={handleSubmit}>Interpretar Archivo de Transferencia</Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// 5.2 — Conciliación de comisiones de tarjeta y retenciones
// ─────────────────────────────────────────────────────────────────────────
function ConciliarComisionesForm({ onConciliar, procesando }) {
  const [comisionPct, setComisionPct] = useState('');
  const [localError, setLocalError] = useState('');

  async function handleSubmit() {
    setLocalError('');
    if (comisionPct === '' || Number(comisionPct) < 0) {
      setLocalError('Ingresa el porcentaje de comisión de tarjeta estimado');
      return;
    }
    const ok = await onConciliar(comisionPct);
    if (!ok) setLocalError('No se pudo conciliar las comisiones y retenciones');
  }

  return (
    <div className="mt-3 flex flex-col gap-2">
      <ErrorBox error={localError} />
      <div className="flex items-end gap-2 flex-wrap">
        <Input
          label="% Comisión de tarjeta estimada"
          name="comisionPct"
          type="number"
          value={comisionPct}
          onChange={(e) => setComisionPct(e.target.value)}
          placeholder="3.5"
          className="max-w-[220px]"
        />
        <Button className="!py-2.5" loading={procesando} onClick={handleSubmit}>
          Conciliar Comisiones y Retenciones
        </Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Tarjeta del cobro con sus pasos
// ─────────────────────────────────────────────────────────────────────────
function CobroCard({ cobro, procesando, onConciliar, onAjustar, onRegistrar }) {
  return (
    <div className="border border-slate-200 rounded-xl p-4 bg-white">
      <div className="flex justify-between items-start flex-wrap gap-2">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-800">Cobro #{cobro.id}</span>
          <div className="text-xs text-slate-400 mt-0.5">Retenciones aplicadas: {fmt(cobro.retenciones)}</div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(cobro.montoTransferido)}</div>
          <div className="mt-1"><EstadoBadge estado={cobro.estado} /></div>
        </div>
      </div>

      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Archivo de transferencia bancaria interpretado" done />

        {cobro.estado !== ESTADO_COBRO_AR.INTERPRETADO && (
          <>
            <StepLine label={`Comisiones y retenciones conciliadas (${cobro.comisionPct}% comisión)`} done />
            <div className={`text-[12.6px] py-0.5 ${cobro.cuadra ? 'text-emerald-700' : 'text-red-600'}`}>
              {cobro.cuadra ? '✓' : '✗'} Monto conciliado {fmt(cobro.montoConciliado)}
              {!cobro.cuadra && cobro.estado === ESTADO_COBRO_AR.DESCUADRADO && ` — diferencia detectada de ${fmt(cobro.diferencia)}`}
            </div>
          </>
        )}

        {cobro.estado === ESTADO_COBRO_AR.AJUSTADO && (
          <StepLine label="Ajuste contable por diferencia ingresado — montos cuadrados" done />
        )}

        {cobro.registrado && (
          <StepLine label="Ingreso de dinero registrado e imputado en la cuenta del cliente" done />
        )}
      </div>

      {cobro.requiereConciliarComisiones && (
        <ConciliarComisionesForm onConciliar={onConciliar} procesando={procesando} />
      )}

      {cobro.requiereAjusteDiferencia && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={onAjustar}>
            Ingresar Ajuste Contable por Diferencia
          </Button>
        </div>
      )}

      {cobro.requiereRegistrarIngreso && (
        <div className="flex justify-end mt-3">
          <Button className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={onRegistrar}>
            Registrar Ingreso e Imputación
          </Button>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Selección del lote (Fase 02) para procesar su cobro
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
        <span className="font-mono font-semibold mr-1">RN: AR5-01 · SAP FI-AR</span>
        El archivo de transferencia bancaria recibido de la entidad recaudadora se interpreta y se concilia
        contra las comisiones de tarjeta y retenciones esperadas; ante un descuadre se ingresa el ajuste
        contable por diferencia, y una vez cuadrado se registra el ingreso e imputación en la cuenta del cliente.
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
          Procesar Cobro del Lote →
        </Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Flujo principal — cobro del lote seleccionado
// ─────────────────────────────────────────────────────────────────────────
function ProcesamientoCobroFlow({ loteId, onCambiarLote }) {
  const navigate = useNavigate();
  const {
    cobro, loading, procesando, error,
    cargarCobro, interpretar, conciliarComisiones, ajustarDiferencia, registrarIngreso,
  } = useCobroAR(loteId);

  useEffect(() => {
    cargarCobro();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loteId]);

  return (
    <div className="p-6 flex flex-col gap-5">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AR5-01 · SAP FI-AR</span>
        El archivo de transferencia bancaria recibido de la entidad recaudadora se interpreta y se concilia
        contra las comisiones de tarjeta y retenciones esperadas.
      </div>

      <ErrorBox error={error} />

      {loading ? (
        <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando cobro del lote...</div>
      ) : !cobro ? (
        <InterpretarArchivoForm onInterpretar={interpretar} procesando={procesando} />
      ) : (
        <CobroCard
          cobro={cobro}
          procesando={procesando}
          onConciliar={conciliarComisiones}
          onAjustar={ajustarDiferencia}
          onRegistrar={registrarIngreso}
        />
      )}

      <div className="flex justify-between items-center flex-wrap gap-3 pt-2">
        <button onClick={onCambiarLote} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
          Procesar otro lote
        </button>
        {cobro?.puedeContinuarFase06 ? (
          <div className="flex flex-col items-end gap-1">
            <Button onClick={() => navigate(`/fico/compensacion-final?contabilizacionARId=${loteId}`)}>
              Continuar a Fase 06 — Compensación Final →
            </Button>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Completa el procesamiento del cobro para continuar el ciclo.</p>
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 05: Procesamiento de Cobros e Imputación Bancaria
// ─────────────────────────────────────────────────────────────────────────
export default function CobroARPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const contabilizacionARIdParam = searchParams.get('contabilizacionARId');

  const [loteId, setLoteId] = useState(contabilizacionARIdParam ?? null);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiArRail activeStep={4} maxReached={4} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: AR5-01 · SAP FI-AR"
          title="Procesamiento de Cobros e Imputación Bancaria"
          description="El archivo de transferencia bancaria recibido de la entidad recaudadora se interpreta y se concilia contra las comisiones de tarjeta y retenciones esperadas; ante un descuadre se ingresa el ajuste contable por diferencia, y una vez cuadrado se registra el ingreso e imputación en la cuenta del cliente."
          badge="FI-AR"
        />

        {loteId ? (
          <ProcesamientoCobroFlow loteId={loteId} onCambiarLote={() => setLoteId(null)} />
        ) : (
          <SeleccionLotePanel loteIdInicial={contabilizacionARIdParam} onSeleccionar={setLoteId} />
        )}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 05 de {CICLO_FI_AR_FASES.length} — el ingreso de dinero debe quedar registrado e imputado para habilitar la Fase 06 (Compensación Final y Análisis de Margen Neto)
      </p>
    </div>
  );
}
