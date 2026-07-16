import { useState, useEffect, useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Select   from '@/presentation/components/ui/Select';
import FiArRail, { CICLO_FI_AR_FASES } from '@/presentation/components/fiAr/FiArRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useDebitosAR } from '@/presentation/hooks/useDebitosAR';
import { useCases } from '@/infrastructure';
import { ESTADO_DEBITO_AR, ESTADO_DEBITO_AR_LABEL } from '@/domain/models/DebitoAR';

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
    [ESTADO_DEBITO_AR.PENDIENTE_JUSTIFICACION]: 'bg-slate-100 text-slate-600 border border-slate-200',
    [ESTADO_DEBITO_AR.JUSTIFICADO]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
    [ESTADO_DEBITO_AR.NO_JUSTIFICADO]: 'bg-red-50 text-red-600 border border-red-200',
    [ESTADO_DEBITO_AR.RECLAMO_TRAMITADO]: 'bg-amber-50 text-amber-700 border border-amber-200',
    [ESTADO_DEBITO_AR.AJUSTADO]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
  };
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[estado] ?? 'bg-slate-100 text-slate-600'}`}>
      {ESTADO_DEBITO_AR_LABEL[estado] ?? estado}
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
// Tarjeta de débito con sus pasos de justificación, reclamo y ajuste técnico
// ─────────────────────────────────────────────────────────────────────────
function DebitoCard({ debito, procesando, onEvaluarJustificacion, onTramitarReclamo, onAplicarAjuste }) {
  return (
    <div className="border border-slate-200 rounded-xl p-4 bg-white">
      <div className="flex justify-between items-start flex-wrap gap-2">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-800">{debito.numeroReceta}</span>
          <div className="text-xs text-slate-400 mt-0.5">{debito.motivo}</div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(debito.monto)}</div>
          <div className="mt-1"><EstadoBadge estado={debito.estado} /></div>
        </div>
      </div>

      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Débito calculado por receta con observación" done />

        {debito.estado !== ESTADO_DEBITO_AR.PENDIENTE_JUSTIFICACION && debito.justificado === true && (
          <StepLine label="Débito justificado — Contabilidad registra el débito" done />
        )}

        {debito.justificado === false && (
          <>
            <div className="text-[12.6px] text-red-600 py-0.5">✗ Débito no justificado</div>
            {debito.tramitado && (
              <StepLine label="Reclamo tramitado — Contabilidad sienta pérdida por débito no subsanable" done />
            )}
          </>
        )}

        {debito.ajustado && (
          <StepLine label="Ajuste técnico contable aplicado" done />
        )}
      </div>

      {debito.requiereEvaluacionJustificacion && (
        <div className="flex justify-end gap-2 mt-3">
          <Button variant="danger" className="!py-1.5 !px-3 !text-xs" loading={procesando}
                  onClick={() => onEvaluarJustificacion(debito.id, false)}>
            ¿Débito Justificado? — No
          </Button>
          <Button className="!py-1.5 !px-3 !text-xs" loading={procesando}
                  onClick={() => onEvaluarJustificacion(debito.id, true)}>
            ¿Débito Justificado? — Sí
          </Button>
        </div>
      )}

      {debito.requiereTramitarReclamo && (
        <div className="flex justify-end mt-3">
          <Button variant="secondary" className="!py-1.5 !px-3 !text-xs" loading={procesando}
                  onClick={() => onTramitarReclamo(debito.id)}>
            Tramitar Reclamo (Área Técnica)
          </Button>
        </div>
      )}

      {debito.requiereAjusteTecnico && (
        <div className="flex justify-end mt-3">
          <Button className="!py-1.5 !px-3 !text-xs" loading={procesando}
                  onClick={() => onAplicarAjuste(debito.id)}>
            Aplicar Ajustes Técnicos Contables
          </Button>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Selección del lote (Fase 03) para conciliar sus débitos
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
        <span className="font-mono font-semibold mr-1">RN: AR4-01 · SAP FI-AR</span>
        Los débitos generados por recetas rechazadas o impugnaciones no aceptadas se evalúan: si son justificados
        se registran contablemente; si no, se tramita el reclamo y se sienta la pérdida correspondiente.
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
          Conciliar Débitos del Lote →
        </Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Flujo principal — débitos del lote seleccionado
// ─────────────────────────────────────────────────────────────────────────
function ConciliacionDebitosFlow({ loteId, onCambiarLote }) {
  const navigate = useNavigate();
  const {
    debitos, loading, procesando, error,
    cargarDebitos, calcularDebito, evaluarJustificacion, tramitarReclamo, aplicarAjusteTecnico,
  } = useDebitosAR(loteId);

  const [recetasConDebito, setRecetasConDebito] = useState([]);
  const [cargandoRecetas, setCargandoRecetas] = useState(true);
  const [puedeContinuar, setPuedeContinuar] = useState(false);

  async function cargarTodo() {
    setCargandoRecetas(true);
    const [recetas] = await Promise.all([
      useCases.recetaMedicaAR.getAll.execute({ contabilizacionARId: loteId }),
      cargarDebitos(),
    ]);
    setRecetasConDebito(recetas.filter((r) => r.generaDebito));
    setCargandoRecetas(false);
  }

  useEffect(() => {
    cargarTodo();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loteId]);

  useEffect(() => {
    async function verificar() {
      if (!loteId) return;
      const ok = await useCases.debitoAR.puedeContinuarFase05.execute(loteId);
      setPuedeContinuar(ok);
    }
    verificar();
  }, [loteId, debitos]);

  const pendientesDeCalcular = useMemo(
    () => recetasConDebito.filter((r) => !debitos.some((d) => d.recetaMedicaARId === r.id)),
    [recetasConDebito, debitos],
  );

  const loading_ = loading || cargandoRecetas;
  const sinDebitos = !loading_ && recetasConDebito.length === 0 && debitos.length === 0;
  const conciliados = debitos.filter((d) => d.conciliado || d.ajustado).length;

  async function handleCalcular(recetaMedicaARId) {
    await calcularDebito(recetaMedicaARId);
  }

  return (
    <div className="p-6 flex flex-col gap-5">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800 flex-1">
          <span className="font-mono font-semibold mr-1">RN: AR4-01 · SAP FI-AR</span>
          Los débitos generados por recetas rechazadas o impugnaciones no aceptadas se evalúan: si son justificados
          se registran contablemente; si no, se tramita el reclamo y se sienta la pérdida correspondiente.
        </div>
        {debitos.length > 0 && (
          <span className="font-mono text-[11px] px-2.5 py-1 rounded-full bg-slate-100 text-slate-600 border border-slate-200 whitespace-nowrap">
            {conciliados}/{debitos.length} conciliados
          </span>
        )}
      </div>

      <ErrorBox error={error} />

      {loading_ ? (
        <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando débitos del lote...</div>
      ) : sinDebitos ? (
        <div className="flex flex-col gap-4">
          <div className="px-4 py-3 bg-emerald-50 border border-emerald-200 rounded-xl text-sm text-emerald-700">
            No se generaron débitos en la Fase 03: todas las recetas fueron aprobadas o liberadas para cobro.
          </div>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {pendientesDeCalcular.map((r) => (
            <div key={r.id} className="border border-dashed border-slate-300 rounded-xl p-4 bg-slate-50 flex justify-between items-center flex-wrap gap-3">
              <div>
                <span className="font-mono font-semibold text-sm text-slate-700">{r.numero}</span>
                <div className="text-xs text-slate-400 mt-0.5">{r.motivoRechazo || r.inconsistencia} · {fmt(r.montoDeclarado)}</div>
              </div>
              <Button className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={() => handleCalcular(r.id)}>
                Calcular Débito
              </Button>
            </div>
          ))}

          {debitos.map((d) => (
            <DebitoCard
              key={d.id}
              debito={d}
              procesando={procesando}
              onEvaluarJustificacion={evaluarJustificacion}
              onTramitarReclamo={tramitarReclamo}
              onAplicarAjuste={aplicarAjusteTecnico}
            />
          ))}
        </div>
      )}

      <div className="flex justify-between items-center flex-wrap gap-3 pt-2">
        <button onClick={onCambiarLote} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
          Conciliar otro lote
        </button>
        {(sinDebitos || puedeContinuar) ? (
          <div className="flex flex-col items-end gap-1">
            <Button onClick={() => navigate(`/fico/cobros?contabilizacionARId=${loteId}`)}>
              Continuar a Fase 05 — Cobros e Imputación Bancaria →
            </Button>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Resuelve cada débito para continuar el ciclo.</p>
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 04: Conciliación de Débitos y Ajustes Técnicos
// ─────────────────────────────────────────────────────────────────────────
export default function DebitoARPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const contabilizacionARIdParam = searchParams.get('contabilizacionARId');

  const [loteId, setLoteId] = useState(contabilizacionARIdParam ?? null);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiArRail activeStep={3} maxReached={3} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: AR4-01 · SAP FI-AR"
          title="Conciliación de Débitos y Ajustes Técnicos"
          description="Los débitos generados por recetas rechazadas o impugnaciones no aceptadas se evalúan: si son justificados se registran contablemente; si no, se tramita el reclamo y se sienta la pérdida correspondiente."
          badge="FI-AR"
        />

        {loteId ? (
          <ConciliacionDebitosFlow loteId={loteId} onCambiarLote={() => setLoteId(null)} />
        ) : (
          <SeleccionLotePanel loteIdInicial={contabilizacionARIdParam} onSeleccionar={setLoteId} />
        )}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 04 de {CICLO_FI_AR_FASES.length} — todos los débitos deben quedar ajustados (o no existir débitos) para habilitar la Fase 05 (Procesamiento de Cobros e Imputación Bancaria)
      </p>
    </div>
  );
}
