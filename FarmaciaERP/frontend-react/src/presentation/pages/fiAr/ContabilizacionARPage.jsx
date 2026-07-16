import { useState, useEffect, useCallback, useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Select   from '@/presentation/components/ui/Select';
import Table    from '@/presentation/components/ui/Table';
import FiArRail, { CICLO_FI_AR_FASES } from '@/presentation/components/fiAr/FiArRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useContabilizacionAR } from '@/presentation/hooks/useContabilizacionAR';
import { useCases } from '@/infrastructure';
import { ESTADO_CONTABILIZACION_AR_LABEL } from '@/domain/models/ContabilizacionAR';

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
    err: 'bg-red-50 border-red-500 text-red-700',
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

// ─────────────────────────────────────────────────────────────────────────
// Selección del cierre de caja e inicio de la Fase 02
// ─────────────────────────────────────────────────────────────────────────
function IniciarContabilizacionPanel({ cierreCajaIdInicial, onIniciada }) {
  const { procesando, error, iniciar } = useContabilizacionAR();
  const [cierres, setCierres] = useState([]);
  const [loadingCierres, setLoadingCierres] = useState(false);
  const [cierreCajaId, setCierreCajaId] = useState(cierreCajaIdInicial ?? '');
  const [localError, setLocalError] = useState('');

  const cargarCierresElegibles = useCallback(async () => {
    setLoadingCierres(true);
    try {
      const todos = await useCases.cierreCaja.getAll.execute({});
      const elegibles = [];
      for (const c of todos.filter((c) => c.puedeContinuarFase02)) {
        const existente = await useCases.contabilizacionAR.getByCierreCaja.execute(c.id);
        if (!existente) elegibles.push(c);
      }
      setCierres(elegibles);
    } finally {
      setLoadingCierres(false);
    }
  }, []);

  useEffect(() => {
    cargarCierresElegibles();
  }, [cargarCierresElegibles]);

  const cierreOptions = cierres.map((c) => ({
    value: c.id,
    label: `${c.numero} — ${c.sucursalNombre} — ${fmt(c.reporteVentas)}`,
  }));

  async function handleIniciar() {
    setLocalError('');
    if (!cierreCajaId) { setLocalError('Selecciona un cierre de caja clasificado en la Fase 01'); return; }

    const nueva = await iniciar(cierreCajaId);
    if (nueva) {
      onIniciada?.(nueva);
    } else {
      setLocalError(error ?? 'No se pudo iniciar la contabilización AR');
    }
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AR2-01 · SAP FI-AR</span>
        Se concilian los lotes de tarjetas, se contabiliza el asiento automatizado de ventas y se audita la
        integridad de las recetas médicas antes de consolidar el lote hacia Oficina Central.
      </div>

      <ErrorBox error={localError || error} />

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">
          Cierre de caja clasificado (Fase 01)
        </p>
        <Select
          name="cierreCajaId"
          placeholder={loadingCierres ? 'Cargando cierres de caja...' : 'Selecciona un cierre de caja...'}
          value={cierreCajaId}
          onChange={(e) => setCierreCajaId(e.target.value)}
          options={cierreOptions}
          disabled={loadingCierres}
        />
        {!loadingCierres && cierreOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay cierres de caja clasificados pendientes de contabilizar. Vuelve a la Fase 01 para clasificar
            copagos y coberturas de un cierre de caja.
          </p>
        )}
      </div>

      <div className="flex justify-end">
        <Button onClick={handleIniciar} loading={procesando} disabled={!cierreCajaId}>
          Iniciar Fase 02 — Contabilización →
        </Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Flujo de la Fase 02 sobre una contabilización ya iniciada
// ─────────────────────────────────────────────────────────────────────────
function ContabilizacionFlow({ contabilizacionInicial, onNuevaContabilizacion }) {
  const navigate = useNavigate();
  const {
    contabilizacion, procesando, error,
    cargarPorId, conciliarLotesPOS, procesarAsiento, revisarAjusteAsientos,
    auditarRecetas, solicitarDuplicadoReceta, reintentarAuditoria, consolidarLote,
  } = useContabilizacionAR();

  const [motivoObservacion, setMotivoObservacion] = useState('');
  const [mostrarFormObservacion, setMostrarFormObservacion] = useState(false);
  const [localError, setLocalError] = useState('');

  useEffect(() => {
    if (contabilizacionInicial?.id) cargarPorId(contabilizacionInicial.id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [contabilizacionInicial?.id]);

  const c = contabilizacion ?? contabilizacionInicial;
  if (!c) return null;

  async function run(accion, mensaje) {
    setLocalError('');
    const res = await accion();
    if (!res) setLocalError(error ?? mensaje);
  }

  async function handleAuditarConforme() {
    await run(() => auditarRecetas({ conforme: true, motivoObservacion: '' }),
      'No se pudo registrar la auditoría de recetas');
  }

  async function handleAuditarObservacion() {
    if (!motivoObservacion.trim()) { setLocalError('Describe el motivo de la observación'); return; }
    const res = await auditarRecetas({ conforme: false, motivoObservacion });
    if (!res) setLocalError(error ?? 'No se pudo registrar la auditoría de recetas');
    else { setMostrarFormObservacion(false); setMotivoObservacion(''); }
  }

  const conVariacion = c.tieneVariacion;

  return (
    <div className="p-6 flex flex-col gap-5">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <p className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Cierre de caja</p>
          <p className="text-sm font-mono text-slate-700">{c.cierreCajaNumero}</p>
        </div>
        <Badge value={ESTADO_CONTABILIZACION_AR_LABEL[c.estado] ? c.estado : undefined} />
      </div>

      <ErrorBox error={localError} />

      {/* Rama de variación — Conciliación primaria de lotes POS */}
      {conVariacion && (
        <div>
          <LaneNote>Personal de Farmacia / Administración</LaneNote>
          <Callout tone="warn">
            <RuleTag>2.1.2</RuleTag>
            Registro de Declaración de Diferencias en Pantalla ERP — variación declarada previamente en la Fase 01.
          </Callout>
          {c.requiereConciliacionPOS && (
            <>
              <div className="mt-3">
                <Callout>
                  <RuleTag>2.1.1</RuleTag>
                  Conciliación Primaria de Lotes de Tarjetas Físicas (POS).
                </Callout>
              </div>
              <div className="flex justify-end mt-4">
                <Button variant="secondary" loading={procesando}
                        onClick={() => run(conciliarLotesPOS, 'No se pudo conciliar los lotes POS')}>
                  Conciliar Lotes de Tarjetas POS
                </Button>
              </div>
            </>
          )}
        </div>
      )}

      {/* Paso 2.2.2 — Procesar asiento contable (solo si ya se resolvió la conciliación POS de ser necesaria) */}
      {(!conVariacion || c.conciliacionPOS) && (
        <div>
          <LaneNote>Sistema ERP</LaneNote>
          {!c.asientoProcesado ? (
            <>
              <Callout>
                <RuleTag>2.2.2</RuleTag>
                Procesar Asiento Automatizado de Ventas y Cuadraturas.
              </Callout>
              <div className="flex justify-end mt-4">
                <Button loading={procesando}
                        onClick={() => run(procesarAsiento, 'No se pudo procesar el asiento contable')}>
                  Procesar Asiento Contable
                </Button>
              </div>
            </>
          ) : (
            <Callout tone="ok">
              <RuleTag>2.2.2</RuleTag>
              Asiento automatizado de ventas y cuadraturas contabilizado. Comprobante generado.
            </Callout>
          )}
        </div>
      )}

      {/* Paso 2.2.3 — Revisión y ajuste de asientos descuadrados */}
      {c.requiereAjusteDescuadre && (
        <div>
          <LaneNote>Personal de Farmacia / Administración</LaneNote>
          <Callout>
            <RuleTag>2.2.3</RuleTag>
            Revisión y Ajuste de Asientos Descuadrados.
          </Callout>
          <div className="flex justify-end mt-4">
            <Button variant="secondary" loading={procesando}
                    onClick={() => run(revisarAjusteAsientos, 'No se pudo revisar el ajuste de asientos')}>
              Revisar y Ajustar Asientos
            </Button>
          </div>
        </div>
      )}

      {/* Paso 2.3 — Auditoría de integridad y firmas de recetas */}
      {c.listaParaAuditoria && (
        <div>
          <LaneNote>Personal de Farmacia / Administración</LaneNote>

          {c.recetasCorrectas === null && (
            <>
              <Callout>
                <RuleTag>2.3.1</RuleTag>
                Auditoría de Integridad y Firmas de Recetas Médicas — cruce contra la pre-liquidación emitida por
                la aseguradora.
              </Callout>
              {!mostrarFormObservacion ? (
                <div className="flex justify-end gap-2 mt-4">
                  <Button variant="secondary" onClick={() => setMostrarFormObservacion(true)}>
                    Registrar observación
                  </Button>
                  <Button loading={procesando} onClick={handleAuditarConforme}>
                    Marcar Recetas como Conformes
                  </Button>
                </div>
              ) : (
                <div className="mt-4 flex flex-col gap-3">
                  <div className="flex flex-col gap-1.5">
                    <label className="text-xs font-semibold text-slate-600 uppercase tracking-wide">
                      Motivo de la observación
                    </label>
                    <textarea
                      className="w-full border border-slate-200 rounded-lg px-3.5 py-2.5 text-sm text-slate-800 outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 min-h-[70px]"
                      placeholder="Ej: troquel ilegible, firma del médico no coincide, receta vencida..."
                      value={motivoObservacion}
                      onChange={(e) => setMotivoObservacion(e.target.value)}
                    />
                  </div>
                  <div className="flex justify-end gap-2">
                    <button onClick={() => { setMostrarFormObservacion(false); setMotivoObservacion(''); }}
                            className="text-xs font-medium text-slate-500 hover:underline">
                      Cancelar
                    </button>
                    <Button variant="danger" loading={procesando} onClick={handleAuditarObservacion}>
                      Registrar Observación de Receta
                    </Button>
                  </div>
                </div>
              )}
            </>
          )}

          {c.recetasObservadasPendientes && (
            <>
              <Callout tone="err">
                ¿Recetas correctas? <b>No</b> — {c.motivoObservacion || 'se detectó una observación en la receta.'}
              </Callout>
              <div className="mt-3">
                <Callout tone="warn">
                  <RuleTag>2.3.3</RuleTag>
                  Subsanación de Recetas y Solicitud de Duplicados a Sucursal / Médico.
                </Callout>
              </div>
              <div className="flex justify-end mt-4">
                <Button variant="secondary" loading={procesando}
                        onClick={() => run(solicitarDuplicadoReceta, 'No se pudo solicitar el duplicado de receta')}>
                  Solicitar Duplicado a la Sucursal
                </Button>
              </div>
            </>
          )}

          {c.duplicadoPendienteReintento && (
            <>
              <Callout>Duplicado recibido de la sucursal. Se reintenta la auditoría de integridad.</Callout>
              <div className="flex justify-end mt-4">
                <Button variant="secondary" loading={procesando}
                        onClick={() => run(reintentarAuditoria, 'No se pudo reintentar la auditoría de recetas')}>
                  Reintentar Auditoría de Recetas
                </Button>
              </div>
            </>
          )}

          {c.recetasConformes && (
            <>
              <Callout tone="ok">¿Recetas correctas? <b>Sí</b> — firmas, troqueles y vigencia conformes.</Callout>
              {!c.consolidado ? (
                <>
                  <div className="mt-3">
                    <Callout>
                      <RuleTag>2.3.2</RuleTag>
                      Consolidación del Lote y Despacho de Valija Física.
                    </Callout>
                  </div>
                  <div className="flex justify-end mt-4">
                    <Button loading={procesando}
                            onClick={() => run(consolidarLote, 'No se pudo consolidar el lote')}>
                      Consolidar Lote y Despachar Valija
                    </Button>
                  </div>
                </>
              ) : (
                <>
                  <div className="mt-3">
                    <Callout tone="ok">
                      Fase 02 AR concluida. Lote consolidado y valija despachada a Oficina Central.
                    </Callout>
                  </div>
                  <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
                    <button onClick={onNuevaContabilizacion} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                      Contabilizar otro cierre de caja
                    </button>
                    <Button onClick={() => navigate(`/fico/recetas?contabilizacionARId=${c.id}`)}>
                      Continuar a Fase 03 — Auditoría Médica →
                    </Button>
                  </div>
                </>
              )}
            </>
          )}
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de contabilizaciones
// ─────────────────────────────────────────────────────────────────────────
function HistorialContabilizacionesTab() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'cierreCajaNumero', label: 'N° Cierre de Caja' },
    { key: 'tieneVariacion', label: 'Con variación', render: (val) => (val ? 'Sí' : 'No') },
    { key: 'consolidado', label: 'Consolidado', render: (val) => (val ? 'Sí' : 'No') },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_CONTABILIZACION_AR_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.contabilizacionAR.getAll.execute({});
      setItems(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las contabilizaciones AR');
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
        <p className="text-sm text-slate-500">{items.length} contabilizaciones AR registradas</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando contabilizaciones...</div>
        ) : (
          <Table columns={columns} data={items} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 02: Contabilización y Declaración de Valores (SAP FI-AR)
// ─────────────────────────────────────────────────────────────────────────
export default function ContabilizacionARPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const cierreCajaIdParam = searchParams.get('cierreCajaId');

  const [tab, setTab] = useState('flujo');
  const [contabilizacionActiva, setContabilizacionActiva] = useState(null);
  const [checandoExistente, setCheandoExistente] = useState(!!cierreCajaIdParam);

  useEffect(() => {
    async function verificarExistente() {
      if (!cierreCajaIdParam) return;
      setCheandoExistente(true);
      const existente = await useCases.contabilizacionAR.getByCierreCaja.execute(cierreCajaIdParam);
      if (existente) setContabilizacionActiva(existente);
      setCheandoExistente(false);
    }
    verificarExistente();
  }, [cierreCajaIdParam]);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiArRail activeStep={1} maxReached={1} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: AR2-01 · SAP FI-AR"
          title="Contabilización y Declaración de Valores"
          description="Se concilian los lotes de tarjetas, se contabiliza el asiento automatizado de ventas y se audita la integridad de las recetas médicas antes de consolidar el lote hacia Oficina Central."
          badge="FI-AR"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Contabilización' },
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

        {tab === 'flujo' ? (
          checandoExistente ? (
            <div className="flex items-center justify-center py-16 text-slate-400 text-sm">Cargando...</div>
          ) : contabilizacionActiva ? (
            <ContabilizacionFlow
              contabilizacionInicial={contabilizacionActiva}
              onNuevaContabilizacion={() => setContabilizacionActiva(null)}
            />
          ) : (
            <IniciarContabilizacionPanel
              cierreCajaIdInicial={cierreCajaIdParam}
              onIniciada={(nueva) => setContabilizacionActiva(nueva)}
            />
          )
        ) : (
          <HistorialContabilizacionesTab />
        )}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 02 de {CICLO_FI_AR_FASES.length} — la consolidación del lote y despacho de la valija habilita la Fase 03 (Auditoría Médica e Impugnación de Recetas)
      </p>
    </div>
  );
}
