import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Table    from '@/presentation/components/ui/Table';
import FiApRail, { CICLO_FI_AP_FASES } from '@/presentation/components/fiAp/FiApRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useLotesPago } from '@/presentation/hooks/useLotesPago';
import { useCases } from '@/infrastructure';
import { ESTADO_LOTE_PAGO, ESTADO_LOTE_PAGO_LABEL } from '@/domain/models/LotePagoTesoreria';

// Descuento por pronto pago que negocia el Gerente de Tesorería (RN-AP4-01, paso 4.2)
const DESCUENTO_PRONTO_PAGO_PCT = 2;

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
// Tarjeta de un lote de pagos (recipe-card del prototipo) — pasos 4.1 a 4.5
// ─────────────────────────────────────────────────────────────────────────
function LoteCard({
  lote, procesando,
  onPriorizar, onNegociar, onPreparar, onVerificarFondos,
  onSometerComite, onCorregir, onEjecutarConciliar,
}) {
  return (
    <div className="border border-slate-200 rounded-xl px-4 py-3.5 mb-3 bg-white">
      <div className="flex justify-between items-center gap-3 flex-wrap">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-900">{lote.numero}</span>
          <span className="text-slate-500 text-sm"> · {lote.detalles.length} factura(s) regularizada(s)</span>
          <div className="text-xs text-slate-500 mt-0.5">
            Monto neto regularizado {fmt(lote.montoNetoRegularizado)}
          </div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(lote.montoLote || lote.montoNetoRegularizado)}</div>
          <span className={[
            'inline-block mt-1 text-[10.5px] font-mono px-2 py-0.5 rounded-full border',
            lote.pagosConciliadosGestion
              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
              : 'bg-amber-100 text-amber-800 border-amber-200',
          ].join(' ')}
          >
            {lote.pagosConciliadosGestion ? 'Conciliado' : (ESTADO_LOTE_PAGO_LABEL[lote.estado]?.split(' —')[0] ?? lote.estado)}
          </span>
        </div>
      </div>

      {/* Detalle de facturas priorizadas por criticidad sanitaria */}
      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <table className="w-full text-xs">
          <thead>
            <tr className="text-left text-slate-400">
              <th className="font-medium py-1">Ajuste</th>
              <th className="font-medium py-1">Proveedor</th>
              {lote.proveedoresCriticosPriorizados && <th className="font-medium py-1">Criticidad</th>}
              <th className="font-medium py-1 text-right">Neto a pagar</th>
            </tr>
          </thead>
          <tbody>
            {lote.detalles.map((d) => (
              <tr key={d.ajusteContableId} className="border-t border-slate-100">
                <td className="py-1.5 font-mono text-emerald-900">{d.numeroAjusteContable}</td>
                <td className="py-1.5 text-slate-600">{d.razonSocialProveedor}</td>
                {lote.proveedoresCriticosPriorizados && (
                  <td className="py-1.5">
                    <span className="inline-block text-[10px] font-mono px-2 py-0.5 rounded-full bg-amber-100 text-amber-800 border border-amber-200">
                      Medicamento esencial
                    </span>
                  </td>
                )}
                <td className="py-1.5 text-right font-mono">{fmt(d.montoNetoPagar)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 4.1 — Priorizar Proveedores Críticos de Medicamentos */}
      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Priorizar Proveedores Críticos de Medicamentos" done={lote.proveedoresCriticosPriorizados} />
        {lote.pendientePriorizar && (
          <div className="flex justify-end mt-3">
            <Button variant="secondary" loading={procesando} onClick={() => onPriorizar(lote.id)}>
              <RuleTag>4.1</RuleTag>Priorizar Proveedores Críticos
            </Button>
          </div>
        )}
      </div>

      {/* 4.2 — Negociar Descuento por Pronto Pago */}
      {lote.proveedoresCriticosPriorizados && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Negociar Descuento por Pronto Pago" done={lote.descuentoProntoPagoNegociado} />
          {lote.pendienteNegociar && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onNegociar(lote.id, DESCUENTO_PRONTO_PAGO_PCT)}>
                <RuleTag>4.2</RuleTag>Negociar Descuento por Pronto Pago
              </Button>
            </div>
          )}
          {lote.descuentoProntoPagoNegociado && (
            <EntryBox>
              Descuento por pronto pago negociado: {lote.descuentoProntoPagoPct}% sobre el neto regularizado ({fmt(lote.montoNetoRegularizado)}).
            </EntryBox>
          )}
        </div>
      )}

      {/* 4.3 — Preparar Lote de Pagos (F110 SAP) */}
      {lote.descuentoProntoPagoNegociado && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Preparar Lote de Pagos (F110 SAP)" done={lote.lotePreparado} />
          {lote.pendientePreparar && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onPreparar(lote.id)}>
                <RuleTag>4.3</RuleTag>Preparar Lote de Pagos
              </Button>
            </div>
          )}
          {lote.lotePreparado && (
            <EntryBox>Monto del lote de pagos (F110): {fmt(lote.montoLote)}</EntryBox>
          )}
        </div>
      )}

      {/* 4.4 — Verificar Fondos y Validar Lote */}
      {lote.lotePreparado && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Verificar Fondos y Validar Lote" done={lote.fondosVerificados} />
          {lote.pendienteVerificarFondos && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onVerificarFondos(lote.id)}>
                <RuleTag>4.4</RuleTag>Verificar Fondos y Validar Lote
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 4.4 (cont.) — ¿Lote Aprobado? Comité Semanal de Tesorería */}
      {lote.fondosVerificados && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine
            label="¿Lote Aprobado? — Comité Semanal de Tesorería"
            done={lote.aprobadoPorComite}
            mark={lote.estado === ESTADO_LOTE_PAGO.CON_OBSERVACIONES ? '!' : undefined}
          />
          {lote.pendienteSometerComite && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onSometerComite(lote.id)}>
                Someter Lote al Comité
              </Button>
            </div>
          )}
          {lote.estado === ESTADO_LOTE_PAGO.CON_OBSERVACIONES && !lote.loteCorregido && (
            <>
              <Callout tone="warn">Con observaciones del comité — requiere corrección del lote antes de volver a someterlo.</Callout>
              <div className="flex justify-end mt-3">
                <Button variant="secondary" loading={procesando} onClick={() => onCorregir(lote.id)}>
                  Corregir Lote según Observaciones
                </Button>
              </div>
            </>
          )}
          {lote.pendienteReSometer && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onSometerComite(lote.id)}>
                Volver a Someter al Comité
              </Button>
            </div>
          )}
          {lote.aprobadoPorComite && (
            <StepLine label="Lote aprobado por el Comité Semanal de Tesorería" done />
          )}
        </div>
      )}

      {/* 4.5 — Ejecutar Pagos y Conciliar en SAP FI-AP */}
      {lote.aprobadoPorComite && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Ejecutar Pagos y Conciliar en SAP FI-AP" done={lote.pagosConciliadosGestion} />
          {lote.pendienteConciliar && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onEjecutarConciliar(lote.id)}>
                Ejecutar Pagos y Conciliar
              </Button>
            </div>
          )}
          {lote.pagosConciliadosGestion && (
            <StepLine label="Pagos conciliados a nivel de gestión — habilita la Fase 05" done />
          )}
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Flujo de estrategia de tesorería (Fase 04)
// ─────────────────────────────────────────────────────────────────────────
function LotesPagoTab() {
  const {
    lotes, loading, procesandoId, error,
    cargarLotes, iniciarLote, priorizarProveedoresCriticos, negociarDescuentoProntoPago,
    prepararLotePagos, verificarFondosYValidarLote, someterLoteAComite, corregirLote,
    ejecutarPagosYConciliar,
  } = useLotesPago();

  const [ajustesRegularizados, setAjustesRegularizados] = useState([]);
  const [loadingAjustes, setLoadingAjustes] = useState(false);
  const [localError, setLocalError] = useState('');

  const cargarAjustesRegularizados = useCallback(async () => {
    setLoadingAjustes(true);
    try {
      const data = await useCases.ajusteContable.getAll.execute({});
      setAjustesRegularizados(data.filter((a) => a.regularizada));
    } catch (err) {
      setLocalError(err.response?.data ?? err.message ?? 'Error al obtener los ajustes regularizados en Fase 03');
    } finally {
      setLoadingAjustes(false);
    }
  }, []);

  useEffect(() => {
    cargarLotes();
    cargarAjustesRegularizados();
  }, [cargarLotes, cargarAjustesRegularizados]);

  const ajustesEnLote = useMemo(
    () => new Set(lotes.flatMap((l) => l.detalles.map((d) => d.ajusteContableId))),
    [lotes],
  );
  const ajustesSinLote = useMemo(
    () => ajustesRegularizados.filter((a) => !ajustesEnLote.has(a.id)),
    [ajustesRegularizados, ajustesEnLote],
  );

  async function handleIniciar() {
    setLocalError('');
    const ids = ajustesSinLote.map((a) => a.id);
    const nuevo = await iniciarLote(ids);
    if (!nuevo) setLocalError(error ?? 'No se pudo iniciar el lote de pagos');
  }

  const todosConciliados = lotes.length > 0 && lotes.every((l) => l.pagosConciliadosGestion);

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AP4-01 · F110 SAP</span>
        Se priorizan los proveedores de medicamentos críticos, se negocian descuentos por pronto pago y se prepara
        el lote de pagos, sujeto a validación de fondos y aprobación del Comité Semanal de Tesorería.
      </div>

      <ErrorBox error={localError || error} />

      {/* Analista de Cuentas por Pagar — inicio del armado del lote */}
      <div>
        <LaneNote>Sistema ERP</LaneNote>
        <Callout>
          Facturas regularizadas en Fase 03 — {loadingAjustes
            ? 'buscando ajustes regularizados...'
            : `${ajustesSinLote.length} factura(s) regularizada(s) pendiente(s) de incluirse en un lote de pagos.`}
        </Callout>

        {ajustesSinLote.length > 0 && (
          <div className="mt-3 flex flex-col gap-2">
            <div className="flex items-center justify-between gap-3 border border-slate-200 rounded-lg px-3.5 py-2.5 flex-wrap">
              <div className="text-sm text-slate-600">
                {ajustesSinLote.map((a) => a.numero).join(', ')}
              </div>
              <Button
                variant="secondary"
                loading={procesandoId === 'nuevo'}
                onClick={handleIniciar}
              >
                Iniciar Lote de Pagos
              </Button>
            </div>
          </div>
        )}
      </div>

      {/* Tesorería — flujo de priorización, negociación, preparación y aprobación del lote */}
      <div>
        <LaneNote>Tesorería</LaneNote>
        {loading ? (
          <div className="text-sm text-slate-400 py-6 text-center">Cargando lotes de pago...</div>
        ) : lotes.length === 0 ? (
          <div className="text-sm text-slate-400 py-6 text-center">
            Aún no hay lotes de pago iniciados. Inicia el lote a partir de las facturas regularizadas.
          </div>
        ) : (
          lotes.map((l) => (
            <LoteCard
              key={l.id}
              lote={l}
              procesando={procesandoId === l.id}
              onPriorizar={priorizarProveedoresCriticos}
              onNegociar={negociarDescuentoProntoPago}
              onPreparar={prepararLotePagos}
              onVerificarFondos={verificarFondosYValidarLote}
              onSometerComite={someterLoteAComite}
              onCorregir={corregirLote}
              onEjecutarConciliar={ejecutarPagosYConciliar}
            />
          ))
        )}
      </div>

      {lotes.length > 0 && (
        todosConciliados ? (
          <div>
            <LaneNote>Sistema ERP</LaneNote>
            <Callout tone="ok">
              <RuleTag>4.5</RuleTag>
              Pagos ejecutados y conciliados a nivel de gestión. Se libera la Fase 05 — Procesamiento Automático y Propuesta de Pago.
            </Callout>
            <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
              <button onClick={() => cargarLotes()} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                Actualizar panel
              </button>
              <div className="flex flex-col items-end gap-1">
                <Button onClick={() => navigate('/fico/fi-ap/propuestas-pago')}>Continuar a Fase 05 — Procesamiento Automático →</Button>
              </div>
            </div>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Completa el flujo de tesorería de cada lote para continuar el ciclo.</p>
        )
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de lotes de pago
// ─────────────────────────────────────────────────────────────────────────
function HistorialLotesTab() {
  const [lotes, setLotes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Lote' },
    { key: 'detalles', label: 'Facturas', render: (val) => (val ?? []).length },
    { key: 'montoNetoRegularizado', label: 'Neto regularizado', render: (val) => fmt(val) },
    { key: 'montoLote', label: 'Monto del lote', render: (val) => fmt(val) },
    { key: 'descuentoProntoPagoPct', label: 'Descuento pronto pago', render: (val) => `${val ?? 0}%` },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_LOTE_PAGO_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.lotePago.getAll.execute();
      setLotes(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los lotes de pago');
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
        <p className="text-sm text-slate-500">{lotes.length} lotes de pago registrados</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando lotes de pago...</div>
        ) : (
          <Table columns={columns} data={lotes} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 04: Estrategia de Tesorería y Riesgo Sanitario
// ─────────────────────────────────────────────────────────────────────────
export default function LotePagoPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiApRail activeStep={3} maxReached={3} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-AP4-01 · F110 SAP"
          title="Estrategia de Tesorería y Riesgo Sanitario"
          description="Se priorizan los proveedores de medicamentos críticos, se negocian descuentos por pronto pago y se prepara el lote de pagos, sujeto a validación de fondos y aprobación del Comité Semanal de Tesorería."
          badge="FI-AP"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Estrategia de tesorería' },
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

        {tab === 'flujo' ? <LotesPagoTab /> : <HistorialLotesTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 04 de {CICLO_FI_AP_FASES.length} — la conciliación de pagos a nivel de gestión habilita la Fase 05 (Procesamiento Automático y Propuesta de Pago)
      </p>
    </div>
  );
}
