import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Button   from '@/presentation/components/ui/Button';
import Table    from '@/presentation/components/ui/Table';
import FiApRail, { CICLO_FI_AP_FASES } from '@/presentation/components/fiAp/FiApRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useAjustesContables } from '@/presentation/hooks/useAjustesContables';
import { useCases } from '@/infrastructure';
import { ESTADO_AJUSTE_CONTABLE_LABEL } from '@/domain/models/AjusteContableRegularizacion';

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
// Tarjeta de regularización contable de un ajuste (recipe-card del prototipo)
// ─────────────────────────────────────────────────────────────────────────
function AjusteCard({
  ajuste, procesando,
  onRecepcionNotaCredito, onGestionarReclamo, onEvaluarEnvio, onRegistrarNC,
  onAsiento, onDesbloquear,
}) {
  return (
    <div className="border border-slate-200 rounded-xl px-4 py-3.5 mb-3 bg-white">
      <div className="flex justify-between items-center gap-3 flex-wrap">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-900">{ajuste.numero}</span>
          <span className="text-slate-500 text-sm"> · {ajuste.razonSocialProveedor}</span>
          <div className="text-xs text-slate-500 mt-0.5">
            Disputa {ajuste.numeroDisputaComercial} — Monto a regularizar {fmt(ajuste.montoRegularizacion)}
          </div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(ajuste.montoRegularizacion)}</div>
          <span className={[
            'inline-block mt-1 text-[10.5px] font-mono px-2 py-0.5 rounded-full border',
            ajuste.regularizada
              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
              : 'bg-amber-100 text-amber-800 border-amber-200',
          ].join(' ')}
          >
            {ajuste.regularizada ? 'Regularizada' : (ESTADO_AJUSTE_CONTABLE_LABEL[ajuste.estado]?.split(' —')[0] ?? ajuste.estado)}
          </span>
        </div>
      </div>

      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label={`Inicio del cierre de transacción para ${ajuste.numeroExcepcion || ajuste.numero}`} done />
      </div>

      {/* 3.1 — pendiente de confirmar recepción de NC */}
      {ajuste.pendienteConfirmarNotaCredito && (
        <div className="flex justify-end gap-3 mt-3 flex-wrap">
          <Button variant="secondary" loading={procesando} onClick={() => onRecepcionNotaCredito(ajuste.id, true)}>
            ¿Se Recibe Nota de Crédito? — Sí
          </Button>
          <Button variant="outline" loading={procesando} onClick={() => onRecepcionNotaCredito(ajuste.id, false)}>
            ¿Se Recibe Nota de Crédito? — No
          </Button>
        </div>
      )}

      {/* Camino A: NC no recibida — gestión de reclamo */}
      {ajuste.recibeNotaCredito === false && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Nota de crédito no recibida inicialmente" done mark="✗" />
          {ajuste.pendienteGestionarReclamo && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onGestionarReclamo(ajuste.id)}>
                Gestionar Reclamo con el Laboratorio
              </Button>
            </div>
          )}
          {ajuste.reclamoGestionado && (
            <StepLine label={`Reclamo gestionado con ${ajuste.razonSocialProveedor}`} done />
          )}
          {ajuste.pendienteEvaluarEnvioNotaCredito && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onEvaluarEnvio(ajuste.id)}>
                {ajuste.razonSocialProveedor}: Evaluar y Enviar Nota de Crédito
              </Button>
            </div>
          )}
          {ajuste.notaCreditoEnviadaProveedor && (
            <StepLine label={`${ajuste.razonSocialProveedor} evaluó y envió la Nota de Crédito`} done />
          )}
        </div>
      )}

      {/* Camino B: NC recibida directamente */}
      {ajuste.recibeNotaCredito === true && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Registrar Nota de Crédito en SAP" done={ajuste.notaCreditoRegistrada} />
          {ajuste.pendienteRegistrarNotaCredito && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onRegistrarNC(ajuste.id)}>
                Registrar Nota de Crédito en SAP
              </Button>
            </div>
          )}
        </div>
      )}

      {/* 3.2 — asiento de regularización */}
      {ajuste.notaCreditoRegistrada && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Ejecutar asiento de regularización por diferencias permitidas" done={ajuste.asientoRegularizacion} />
          {ajuste.pendienteAsientoRegularizacion && (
            <div className="flex justify-end mt-3">
              <Button variant="secondary" loading={procesando} onClick={() => onAsiento(ajuste.id)}>
                Ejecutar Asiento de Regularización
              </Button>
            </div>
          )}
          {ajuste.asientoRegularizacion && (
            <EntryBox>
              Asiento contable — (Debe) Gasto por Variación de Precios {fmt(ajuste.montoRegularizacion)} / (Haber) Proveedores · {ajuste.razonSocialProveedor}
            </EntryBox>
          )}
        </div>
      )}

      {/* 3.3 — desbloqueo de partida */}
      {ajuste.asientoRegularizacion && (
        <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
          <StepLine label="Desbloquear partida presupuestaria / factura en MRBR" done={ajuste.desbloqueado} />
          {ajuste.pendienteDesbloquearPartida && (
            <div className="flex justify-end mt-3">
              <Button loading={procesando} onClick={() => onDesbloquear(ajuste.id)}>
                Desbloquear Partida y Actualizar Estado de Pago
              </Button>
            </div>
          )}
          {ajuste.regularizada && (
            <StepLine label="Regularización completa — factura lista para pago" done />
          )}
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Flujo de regularización contable (Fase 03)
// ─────────────────────────────────────────────────────────────────────────
function AjustesContablesTab() {
  const {
    ajustes, loading, procesandoId, error,
    cargarAjustes, iniciarAjuste, registrarRecepcionNotaCredito,
    gestionarReclamo, evaluarEnvioNotaCredito, registrarNotaCredito,
    ejecutarAsientoRegularizacion, desbloquearPartida,
  } = useAjustesContables();

  const [disputasResueltas, setDisputasResueltas] = useState([]);
  const [loadingDisputas, setLoadingDisputas] = useState(false);
  const [localError, setLocalError] = useState('');

  const cargarDisputasResueltas = useCallback(async () => {
    setLoadingDisputas(true);
    try {
      const data = await useCases.disputaComercial.getAll.execute({});
      setDisputasResueltas(data.filter((d) => d.resueltaWorkflow));
    } catch (err) {
      setLocalError(err.response?.data ?? err.message ?? 'Error al obtener las disputas resueltas en Fase 02');
    } finally {
      setLoadingDisputas(false);
    }
  }, []);

  useEffect(() => {
    cargarAjustes();
    cargarDisputasResueltas();
  }, [cargarAjustes, cargarDisputasResueltas]);

  const disputasSinAjuste = useMemo(
    () => disputasResueltas.filter(
      (d) => !ajustes.some((a) => a.disputaComercialId === d.id),
    ),
    [disputasResueltas, ajustes],
  );

  async function handleIniciar(disputaComercialId) {
    setLocalError('');
    const nuevo = await iniciarAjuste(disputaComercialId);
    if (!nuevo) setLocalError(error ?? 'No se pudo iniciar el ajuste contable');
  }

  const todasRegularizadas = ajustes.length > 0 && ajustes.every((a) => a.regularizada);

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AP3-01 · SAP FI-AP</span>
        El Área Financiera confirma la recepción de la Nota de Crédito del laboratorio; si no llega, se gestiona el
        reclamo directamente. En ambos casos se ejecuta el asiento contable y se desbloquea la factura para pago.
      </div>

      <ErrorBox error={localError || error} />

      {/* Sistema ERP — disputas resueltas listas para el cierre de transacción */}
      <div>
        <LaneNote>Sistema ERP</LaneNote>
        <Callout>
          Disputa comercial resuelta — {loadingDisputas
            ? 'buscando disputas resueltas en Fase 02...'
            : `${disputasSinAjuste.length} disputa(s) resuelta(s) pendiente(s) de iniciar el cierre de transacción.`}
        </Callout>

        {disputasSinAjuste.length > 0 && (
          <div className="mt-3 flex flex-col gap-2">
            {disputasSinAjuste.map((d) => (
              <div key={d.id} className="flex items-center justify-between gap-3 border border-slate-200 rounded-lg px-3.5 py-2.5 flex-wrap">
                <div className="text-sm">
                  <span className="font-mono font-semibold text-emerald-900">{d.numero}</span>
                  <span className="text-slate-500"> · Excepción {d.numeroExcepcion} · {d.razonSocialProveedor}</span>
                </div>
                <Button
                  variant="secondary"
                  loading={procesandoId === 'nuevo'}
                  onClick={() => handleIniciar(d.id)}
                >
                  Iniciar Cierre de Transacción
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Área Financiera — regularización */}
      <div>
        <LaneNote>Área Financiera</LaneNote>
        {loading ? (
          <div className="text-sm text-slate-400 py-6 text-center">Cargando ajustes contables...</div>
        ) : ajustes.length === 0 ? (
          <div className="text-sm text-slate-400 py-6 text-center">
            Aún no hay ajustes contables iniciados. Inicia el cierre de transacción desde una disputa resuelta.
          </div>
        ) : (
          ajustes.map((a) => (
            <AjusteCard
              key={a.id}
              ajuste={a}
              procesando={procesandoId === a.id}
              onRecepcionNotaCredito={registrarRecepcionNotaCredito}
              onGestionarReclamo={gestionarReclamo}
              onEvaluarEnvio={evaluarEnvioNotaCredito}
              onRegistrarNC={registrarNotaCredito}
              onAsiento={ejecutarAsientoRegularizacion}
              onDesbloquear={desbloquearPartida}
            />
          ))
        )}
      </div>

      {ajustes.length > 0 && (
        todasRegularizadas ? (
          <div>
            <LaneNote>Sistema ERP</LaneNote>
            <Callout tone="ok">
              <RuleTag>3.3</RuleTag>
              Facturas regularizadas contablemente. Se libera la Fase 04 — Estrategia de Tesorería y Riesgo Sanitario.
            </Callout>
            <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
              <button onClick={() => cargarAjustes()} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                Actualizar panel
              </button>
              <div className="flex flex-col items-end gap-1">
                <Button onClick={() => navigate('/fico/fi-ap/lotes-pago')}>Continuar a Fase 04 — Estrategia de Tesorería →</Button>
              </div>
            </div>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Regulariza cada factura para continuar el ciclo.</p>
        )
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de ajustes contables
// ─────────────────────────────────────────────────────────────────────────
function HistorialAjustesTab() {
  const [ajustes, setAjustes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Ajuste' },
    { key: 'numeroDisputaComercial', label: 'Disputa (Fase 02)' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'montoRegularizacion', label: 'Monto regularizado', render: (val) => fmt(val) },
    {
      key: 'recibeNotaCredito',
      label: 'Nota de Crédito',
      render: (val) => (val === null ? 'Pendiente' : (val ? 'Recibida' : 'Reclamo')),
    },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_AJUSTE_CONTABLE_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.ajusteContable.getAll.execute({});
      setAjustes(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los ajustes contables');
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
        <p className="text-sm text-slate-500">{ajustes.length} ajustes contables registrados</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando ajustes contables...</div>
        ) : (
          <Table columns={columns} data={ajustes} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 03: Ajustes Contables y Regularización (Cierre de Transacción)
// ─────────────────────────────────────────────────────────────────────────
export default function AjusteContablePage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiApRail activeStep={2} maxReached={2} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-AP3-01 · SAP FI-AP"
          title="Ajustes Contables y Regularización (Cierre de Transacción)"
          description="El Área Financiera confirma la recepción de la Nota de Crédito del laboratorio; si no llega, se gestiona el reclamo directamente. En ambos casos se ejecuta el asiento contable y se desbloquea la factura para pago."
          badge="FI-AP"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Regularización contable' },
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

        {tab === 'flujo' ? <AjustesContablesTab /> : <HistorialAjustesTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 03 de {CICLO_FI_AP_FASES.length} — el desbloqueo de la partida presupuestaria habilita la Fase 04 (Estrategia de Tesorería y Riesgo Sanitario)
      </p>
    </div>
  );
}
