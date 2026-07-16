import { useState, useEffect, useCallback, useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import FiArRail, { CICLO_FI_AR_FASES } from '@/presentation/components/fiAr/FiArRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useRecetasMedicasAR } from '@/presentation/hooks/useRecetasMedicasAR';
import { useCases } from '@/infrastructure';
import { ESTADO_RECETA_AR, ESTADO_RECETA_AR_LABEL } from '@/domain/models/RecetaMedicaAR';

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
    [ESTADO_RECETA_AR.PENDIENTE]: 'bg-slate-100 text-slate-600 border border-slate-200',
    [ESTADO_RECETA_AR.VALIDANDO]: 'bg-amber-50 text-amber-700 border border-amber-200',
    [ESTADO_RECETA_AR.RECHAZADA]: 'bg-red-50 text-red-600 border border-red-200',
    [ESTADO_RECETA_AR.APROBADA]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
    [ESTADO_RECETA_AR.IMPUGNANDO]: 'bg-amber-50 text-amber-700 border border-amber-200',
    [ESTADO_RECETA_AR.LIBERADA]: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
    [ESTADO_RECETA_AR.DEBITO]: 'bg-red-50 text-red-600 border border-red-200',
  };
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[estado] ?? 'bg-slate-100 text-slate-600'}`}>
      {ESTADO_RECETA_AR_LABEL[estado] ?? estado}
    </span>
  );
}

function StepLine({ label, done, pendText }) {
  return (
    <div className={`text-[12.6px] py-0.5 ${done ? 'text-emerald-700' : 'text-slate-400'}`}>
      {done ? '✓' : '○'} {label}{!done && pendText ? ` — ${pendText}` : ''}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Tarjeta de receta médica con sus pasos de auditoría e impugnación
// ─────────────────────────────────────────────────────────────────────────
function RecetaCard({ receta, procesando, onValidar, onComparar, onRespuesta }) {
  const [formMotivo, setFormMotivo] = useState(false);
  const [motivoRechazo, setMotivoRechazo] = useState('');
  const [formInconsistencia, setFormInconsistencia] = useState(false);
  const [inconsistencia, setInconsistencia] = useState('');

  async function handleValido() {
    await onValidar(receta.id, { valido: true, motivoRechazo: '' });
  }
  async function handleRechazar() {
    if (!motivoRechazo.trim()) return;
    const ok = await onValidar(receta.id, { valido: false, motivoRechazo });
    if (ok) { setFormMotivo(false); setMotivoRechazo(''); }
  }
  async function handleCoincide() {
    await onComparar(receta.id, { coincide: true, inconsistencia: '' });
  }
  async function handleNoCoincide() {
    if (!inconsistencia.trim()) return;
    const ok = await onComparar(receta.id, { coincide: false, inconsistencia });
    if (ok) { setFormInconsistencia(false); setInconsistencia(''); }
  }

  return (
    <div className="border border-slate-200 rounded-xl p-4 bg-white">
      <div className="flex justify-between items-start flex-wrap gap-2">
        <div>
          <span className="font-mono font-semibold text-sm text-emerald-800">{receta.numero}</span>
          <span className="text-sm text-slate-600"> · {receta.medicamento}</span>
          <div className="text-xs text-slate-400 mt-0.5">{receta.aseguradora} · Monto declarado {fmt(receta.montoDeclarado)}</div>
        </div>
        <div className="text-right">
          <div className="font-mono font-semibold text-sm">{fmt(receta.montoDeclarado)}</div>
          <div className="mt-1"><EstadoBadge estado={receta.estado} /></div>
        </div>
      </div>

      <div className="mt-3 pt-3 border-t border-dashed border-slate-200">
        <StepLine label="Recepción física de receta médica" done />
        <StepLine label="Validación de troqueles, firmas y vigencia" done={!receta.requiereValidacion} pendText="pendiente de validar" />

        {receta.estado === ESTADO_RECETA_AR.RECHAZADA && (
          <>
            <StepLine label="Rechazo registrado en SAP (ZFMR_RECHAZO)" done />
            <div className="text-[12.6px] text-emerald-700 py-0.5">Motivo: {receta.motivoRechazo}</div>
          </>
        )}

        {receta.estado !== ESTADO_RECETA_AR.PENDIENTE && receta.estado !== ESTADO_RECETA_AR.RECHAZADA && (
          <StepLine label="Comparación contra pre-liquidación de la aseguradora" done={receta.estado !== ESTADO_RECETA_AR.VALIDANDO} />
        )}

        {receta.estado === ESTADO_RECETA_AR.APROBADA && (
          <div className="text-[12.6px] text-emerald-700 py-0.5">✓ Coincide con pre-liquidación — receta aprobada para liquidación</div>
        )}

        {['IMPUGNANDO', 'LIBERADA', 'DEBITO'].includes(receta.estado) && (
          <>
            <div className="text-[12.6px] text-amber-700 py-0.5">✗ No coincide — {receta.inconsistencia}</div>
            <StepLine label="Impugnación registrada en SAP (ZFMR_IMPUGNACION)" done />
            {receta.estado === ESTADO_RECETA_AR.LIBERADA && (
              <div className="text-[12.6px] text-emerald-700 py-0.5">✓ Impugnación aceptada — receta liberada para cobro</div>
            )}
            {receta.estado === ESTADO_RECETA_AR.DEBITO && (
              <div className="text-[12.6px] text-red-600 py-0.5">✗ Impugnación rechazada — débito confirmado / pérdida</div>
            )}
          </>
        )}
      </div>

      {/* Acciones según el estado */}
      {receta.requiereValidacion && (
        !formMotivo ? (
          <div className="flex justify-end gap-2 mt-3">
            <Button variant="secondary" className="!py-1.5 !px-3 !text-xs" onClick={() => setFormMotivo(true)}>
              Rechazar receta
            </Button>
            <Button className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={handleValido}>
              Validar Troqueles y Firmas
            </Button>
          </div>
        ) : (
          <div className="mt-3 flex flex-col gap-2">
            <textarea
              className="w-full border border-slate-200 rounded-lg px-3 py-2 text-sm text-slate-800 outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 min-h-[60px]"
              placeholder="Motivo del rechazo (ej: falta firma y sello del médico tratante)..."
              value={motivoRechazo}
              onChange={(e) => setMotivoRechazo(e.target.value)}
            />
            <div className="flex justify-end gap-2">
              <button onClick={() => { setFormMotivo(false); setMotivoRechazo(''); }} className="text-xs text-slate-500 hover:underline">Cancelar</button>
              <Button variant="danger" className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={handleRechazar}>
                Confirmar Rechazo
              </Button>
            </div>
          </div>
        )
      )}

      {receta.requiereComparacion && (
        !formInconsistencia ? (
          <div className="flex justify-end gap-2 mt-3">
            <Button variant="secondary" className="!py-1.5 !px-3 !text-xs" onClick={() => setFormInconsistencia(true)}>
              No coincide
            </Button>
            <Button className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={handleCoincide}>
              Coincide vs. Pre-liquidación
            </Button>
          </div>
        ) : (
          <div className="mt-3 flex flex-col gap-2">
            <textarea
              className="w-full border border-slate-200 rounded-lg px-3 py-2 text-sm text-slate-800 outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 min-h-[60px]"
              placeholder="Inconsistencia detectada (ej: cantidad dispensada excede cobertura del plan)..."
              value={inconsistencia}
              onChange={(e) => setInconsistencia(e.target.value)}
            />
            <div className="flex justify-end gap-2">
              <button onClick={() => { setFormInconsistencia(false); setInconsistencia(''); }} className="text-xs text-slate-500 hover:underline">Cancelar</button>
              <Button variant="danger" className="!py-1.5 !px-3 !text-xs" loading={procesando} onClick={handleNoCoincide}>
                Generar Impugnación
              </Button>
            </div>
          </div>
        )
      )}

      {receta.requiereRespuestaAseguradora && (
        <div className="flex justify-end gap-2 mt-3">
          <Button variant="danger" className="!py-1.5 !px-3 !text-xs" loading={procesando}
                  onClick={() => onRespuesta(receta.id, { aceptaImpugnacion: false })}>
            Aseguradora rechaza
          </Button>
          <Button className="!py-1.5 !px-3 !text-xs" loading={procesando}
                  onClick={() => onRespuesta(receta.id, { aceptaImpugnacion: true })}>
            Aseguradora acepta impugnación
          </Button>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Formulario de recepción física de una nueva receta
// ─────────────────────────────────────────────────────────────────────────
function RegistrarRecetaForm({ onRegistrar, procesando }) {
  const [numero, setNumero] = useState('');
  const [medicamento, setMedicamento] = useState('');
  const [aseguradora, setAseguradora] = useState('');
  const [montoDeclarado, setMontoDeclarado] = useState('');
  const [montoPreliquidado, setMontoPreliquidado] = useState('');
  const [localError, setLocalError] = useState('');
  const [abierto, setAbierto] = useState(false);

  async function handleSubmit() {
    setLocalError('');
    if (!numero.trim() || !medicamento.trim() || !aseguradora.trim() || !montoDeclarado) {
      setLocalError('Completa N° de receta, medicamento, aseguradora y monto declarado');
      return;
    }
    const receta = await onRegistrar({ numero, medicamento, aseguradora, montoDeclarado, montoPreliquidado });
    if (receta) {
      setNumero(''); setMedicamento(''); setAseguradora(''); setMontoDeclarado(''); setMontoPreliquidado('');
      setAbierto(false);
    } else {
      setLocalError('No se pudo registrar la receta médica');
    }
  }

  if (!abierto) {
    return (
      <div className="flex justify-end">
        <Button variant="secondary" onClick={() => setAbierto(true)}>+ Registrar recepción de receta</Button>
      </div>
    );
  }

  return (
    <div className="border border-slate-200 rounded-xl p-4 bg-slate-50 flex flex-col gap-3">
      <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Recepción física de receta médica</p>
      <ErrorBox error={localError} />
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
        <Input label="N° Receta" name="numero" value={numero} onChange={(e) => setNumero(e.target.value)} placeholder="RM-2201" />
        <Input label="Aseguradora" name="aseguradora" value={aseguradora} onChange={(e) => setAseguradora(e.target.value)} placeholder="Rímac Salud" />
        <Input label="Medicamento" name="medicamento" value={medicamento} onChange={(e) => setMedicamento(e.target.value)} placeholder="Amoxicilina 500mg x21" className="sm:col-span-2" />
        <Input label="Monto declarado" name="montoDeclarado" type="number" value={montoDeclarado} onChange={(e) => setMontoDeclarado(e.target.value)} placeholder="S/ 0.00" />
        <Input label="Monto pre-liquidado (aseguradora)" name="montoPreliquidado" type="number" value={montoPreliquidado} onChange={(e) => setMontoPreliquidado(e.target.value)} placeholder="S/ 0.00" />
      </div>
      <div className="flex justify-end gap-2">
        <button onClick={() => setAbierto(false)} className="text-xs text-slate-500 hover:underline">Cancelar</button>
        <Button loading={procesando} onClick={handleSubmit}>Registrar Receta</Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Selección del lote consolidado (Fase 02) para auditar sus recetas
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
        <span className="font-mono font-semibold mr-1">RN: AR3-01 · ZFMR_RECHAZO / ZFMR_IMPUGNACION</span>
        Cada receta física se valida contra requisitos médico-administrativos y se coteja con la pre-liquidación
        de la aseguradora. Las inconsistencias generan una impugnación formal ante la Obra Social / Aseguradora.
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
            No hay lotes consolidados pendientes de auditoría médica. Vuelve a la Fase 02 para consolidar un lote
            y despachar la valija física.
          </p>
        )}
      </div>

      <div className="flex justify-end">
        <Button onClick={() => onSeleccionar(loteId)} disabled={!loteId}>
          Auditar Recetas del Lote →
        </Button>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Flujo principal — recetas del lote seleccionado
// ─────────────────────────────────────────────────────────────────────────
function AuditoriaRecetasFlow({ loteId, onCambiarLote }) {
  const navigate = useNavigate();
  const {
    recetas, loading, procesando, error,
    cargarRecetas, registrarReceta, validarTroquelesFirmas, compararPreliquidacion, registrarRespuestaAseguradora,
  } = useRecetasMedicasAR(loteId);

  useEffect(() => {
    cargarRecetas();
  }, [cargarRecetas]);

  const procesadas = recetas.filter((r) => r.esTerminal).length;
  const allDone = recetas.length > 0 && procesadas === recetas.length;

  return (
    <div className="p-6 flex flex-col gap-5">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800 flex-1">
          <span className="font-mono font-semibold mr-1">RN: AR3-01 · ZFMR_RECHAZO / ZFMR_IMPUGNACION</span>
          Cada receta física se valida contra requisitos médico-administrativos y se coteja con la pre-liquidación
          de la aseguradora.
        </div>
        <span className="font-mono text-[11px] px-2.5 py-1 rounded-full bg-slate-100 text-slate-600 border border-slate-200 whitespace-nowrap">
          {procesadas}/{recetas.length} procesadas
        </span>
      </div>

      <ErrorBox error={error} />

      <RegistrarRecetaForm onRegistrar={registrarReceta} procesando={procesando} />

      {loading ? (
        <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando recetas del lote...</div>
      ) : recetas.length === 0 ? (
        <div className="text-center py-10 text-sm text-slate-400">
          Aún no hay recetas registradas para este lote. Registra la recepción física de la primera receta.
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {recetas.map((r) => (
            <RecetaCard
              key={r.id}
              receta={r}
              procesando={procesando}
              onValidar={validarTroquelesFirmas}
              onComparar={compararPreliquidacion}
              onRespuesta={registrarRespuestaAseguradora}
            />
          ))}
        </div>
      )}

      <div className="flex justify-between items-center flex-wrap gap-3 pt-2">
        <button onClick={onCambiarLote} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
          Auditar otro lote
        </button>
        {allDone ? (
          <div className="flex flex-col items-end gap-1">
            <Button onClick={() => navigate(`/fico/debitos?contabilizacionARId=${loteId}`)}>
              Continuar a Fase 04 — Conciliación de Débitos →
            </Button>
          </div>
        ) : (
          <p className="text-xs text-slate-400">Procesa cada receta médica para continuar el ciclo.</p>
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 03: Auditoría Médica e Impugnación de Recetas
// ─────────────────────────────────────────────────────────────────────────
export default function RecetaMedicaARPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const contabilizacionARIdParam = searchParams.get('contabilizacionARId');

  const [loteId, setLoteId] = useState(contabilizacionARIdParam ?? null);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiArRail activeStep={2} maxReached={2} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: AR3-01 · ZFMR_RECHAZO / ZFMR_IMPUGNACION"
          title="Auditoría Médica e Impugnación de Recetas"
          description="Cada receta física se valida contra requisitos médico-administrativos y se coteja con la pre-liquidación de la aseguradora. Las inconsistencias generan una impugnación formal ante la Obra Social / Aseguradora."
          badge="FI-AR"
        />

        {loteId ? (
          <AuditoriaRecetasFlow loteId={loteId} onCambiarLote={() => setLoteId(null)} />
        ) : (
          <SeleccionLotePanel loteIdInicial={contabilizacionARIdParam} onSeleccionar={setLoteId} />
        )}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 03 de {CICLO_FI_AR_FASES.length} — todas las recetas deben quedar en estado terminal (rechazada, aprobada, liberada o débito) para habilitar la Fase 04 (Conciliación de Débitos y Ajustes Técnicos)
      </p>
    </div>
  );
}
