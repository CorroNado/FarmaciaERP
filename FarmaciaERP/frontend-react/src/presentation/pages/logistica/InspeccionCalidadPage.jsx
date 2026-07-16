import { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import Table    from '@/presentation/components/ui/Table';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Select   from '@/presentation/components/ui/Select';
import Modal    from '@/presentation/components/ui/Modal';
import LogisticaRail, { BLOQUE_A_FASES } from '@/presentation/components/logistica/LogisticaRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useInspeccionCalidad } from '@/presentation/hooks/useInspeccionCalidad';
import { useMigo } from '@/presentation/hooks/useMigo';
import { EntradaMercancia, ESTADO_MIGO } from '@/domain/models/EntradaMercancia';
import { InspeccionCalidad, DECISION_QA } from '@/domain/models/InspeccionCalidad';

const DECISION_BADGE = {
  [DECISION_QA.APROBADO]: 'ACTIVO',
  [DECISION_QA.RECHAZADO]: 'INACTIVO',
};

const DECISION_LABEL = {
  [DECISION_QA.APROBADO]: 'Aprobado — Libre utilización',
  [DECISION_QA.RECHAZADO]: 'Rechazado — Devolución a proveedor',
};

// ─────────────────────────────────────────────────────────────────────────
// TAB: Inspeccionar lote (LOG.06 — QA11, a partir de una entrada MIGO en cuarentena)
// ─────────────────────────────────────────────────────────────────────────
function InspeccionarLoteTab({ onProcesado }) {
  const { entradas, loading: loadingMigo } = useMigo();
  const { inspecciones, aprobarLote, rechazarLote, procesando, error } = useInspeccionCalidad();

  const [entradaMercanciaId, setEntradaMercanciaId] = useState('');
  const [muestreoConforme, setMuestreoConforme] = useState(false);
  const [registroSanitarioVigente, setRegistroSanitarioVigente] = useState(false);
  const [empaqueConforme, setEmpaqueConforme] = useState(false);
  const [motivoRechazo, setMotivoRechazo] = useState('');
  const [mostrarRechazo, setMostrarRechazo] = useState(false);
  const [localError, setLocalError] = useState('');

  // RN-E6-002 / RN-E5-001: solo procede sobre lotes REGISTRADA (en cuarentena) que
  // aún no tengan una Decisión de Empleo (aprobada o rechazada) registrada.
  const idsInspeccionados = useMemo(
    () => new Set(inspecciones.map((i) => i.entradaMercanciaId)),
    [inspecciones]
  );
  const loteElegibles = useMemo(
    () => entradas.filter((e) => e.estado === ESTADO_MIGO.REGISTRADA && !idsInspeccionados.has(e.id)),
    [entradas, idsInspeccionados]
  );
  const loteOptions = loteElegibles.map((e) => ({
    value: e.id,
    label: `${e.numero} — Lote ${e.lote} — ${e.razonSocialProveedor}`,
  }));

  const loteSeleccionado = loteElegibles.find((e) => String(e.id) === String(entradaMercanciaId));
  // RN-E5-002: la cadena de frío llega precalculada desde la recepción (MIGO)
  const cadenaFrioConforme = loteSeleccionado ? !EntradaMercancia.temperaturaFueraDeRango(loteSeleccionado.temperaturaArribo) : false;

  const todosConformes = InspeccionCalidad.todosConformes({
    muestreoConforme, cadenaFrioConforme, registroSanitarioVigente, empaqueConforme,
  });

  function resetForm() {
    setEntradaMercanciaId('');
    setMuestreoConforme(false);
    setRegistroSanitarioVigente(false);
    setEmpaqueConforme(false);
    setMotivoRechazo('');
    setMostrarRechazo(false);
  }

  async function handleAprobar() {
    setLocalError('');
    if (!loteSeleccionado) {
      setLocalError('Selecciona un lote en cuarentena para inspeccionar');
      return;
    }
    if (!todosConformes) {
      setLocalError('RN-E5-006: todos los controles deben estar conformes (incluida la cadena de frío) antes de aprobar el lote');
      return;
    }
    const inspeccion = await aprobarLote({
      entradaMercanciaId: loteSeleccionado.id,
      muestreoConforme, registroSanitarioVigente, empaqueConforme,
    });
    if (inspeccion) {
      resetForm();
      onProcesado?.();
    } else {
      setLocalError(error ?? 'No se pudo aprobar el lote');
    }
  }

  async function handleRechazar() {
    setLocalError('');
    if (!loteSeleccionado) {
      setLocalError('Selecciona un lote en cuarentena para inspeccionar');
      return;
    }
    if (!mostrarRechazo) {
      setMostrarRechazo(true);
      return;
    }
    if (!motivoRechazo.trim()) {
      setLocalError('RN-E5-007: debe indicar el motivo del rechazo');
      return;
    }
    const inspeccion = await rechazarLote({
      entradaMercanciaId: loteSeleccionado.id,
      motivoRechazo, muestreoConforme, registroSanitarioVigente, empaqueConforme,
    });
    if (inspeccion) {
      resetForm();
      onProcesado?.();
    } else {
      setLocalError(error ?? 'No se pudo rechazar el lote');
    }
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN-E5-001 · RN-E5-005 · RN-E5-006</span>
        El Regente Farmacéutico ejecuta el muestreo técnico, valida la cadena de frío, el registro sanitario y el empaque antes de emitir la Decisión de Empleo.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. Lote en cuarentena</p>
        <Select
          name="entradaMercanciaId"
          placeholder={loadingMigo ? 'Cargando entradas MIGO...' : 'Selecciona un lote en cuarentena...'}
          value={entradaMercanciaId}
          onChange={(e) => { setEntradaMercanciaId(e.target.value); setMuestreoConforme(false); setRegistroSanitarioVigente(false); setEmpaqueConforme(false); setMostrarRechazo(false); setMotivoRechazo(''); }}
          options={loteOptions}
          disabled={loadingMigo}
        />
        {!loadingMigo && loteOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay lotes en cuarentena pendientes de inspección. Vuelve a la Fase 04 para registrar una entrada MIGO.
          </p>
        )}
      </div>

      {loteSeleccionado && (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="flex flex-col gap-1.5">
              <span className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Lote en inspección</span>
              <div className="border border-slate-200 bg-slate-50 rounded-lg px-3.5 py-2.5 text-sm text-slate-700 font-mono">
                {loteSeleccionado.lote}
              </div>
            </div>
            <div className="flex flex-col gap-1.5">
              <span className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Temperatura registrada en recepción</span>
              <div className="border border-slate-200 bg-slate-50 rounded-lg px-3.5 py-2.5 text-sm text-slate-700">
                {loteSeleccionado.temperaturaArribo} °C
              </div>
            </div>
          </div>

          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Controles de calidad</p>
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-slate-200">
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">Control</th>
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">Regla</th>
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">Resultado</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  <tr>
                    <td className="px-4 py-3.5 text-slate-700">Muestreo técnico</td>
                    <td className="px-4 py-3.5 font-mono text-xs text-slate-500">RN-E5-001</td>
                    <td className="px-4 py-3.5">
                      <label className="flex items-center gap-2 cursor-pointer">
                        <input type="checkbox" checked={muestreoConforme} onChange={(e) => setMuestreoConforme(e.target.checked)} />
                        Conforme
                      </label>
                    </td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-700">Cadena de frío (2–8 °C)</td>
                    <td className="px-4 py-3.5 font-mono text-xs text-slate-500">RN-E5-002</td>
                    <td className="px-4 py-3.5">
                      <Badge value={cadenaFrioConforme ? 'ACTIVO' : 'INACTIVO'} />
                      <span className="ml-2 text-xs text-slate-500">{cadenaFrioConforme ? 'Dentro de rango' : 'Fuera de rango'}</span>
                    </td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-700">Registro sanitario vigente</td>
                    <td className="px-4 py-3.5 font-mono text-xs text-slate-500">RN-E5-003</td>
                    <td className="px-4 py-3.5">
                      <label className="flex items-center gap-2 cursor-pointer">
                        <input type="checkbox" checked={registroSanitarioVigente} onChange={(e) => setRegistroSanitarioVigente(e.target.checked)} />
                        Vigente
                      </label>
                    </td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-700">Empaque íntegro / hermético</td>
                    <td className="px-4 py-3.5 font-mono text-xs text-slate-500">RN-E5-004</td>
                    <td className="px-4 py-3.5">
                      <label className="flex items-center gap-2 cursor-pointer">
                        <input type="checkbox" checked={empaqueConforme} onChange={(e) => setEmpaqueConforme(e.target.checked)} />
                        Conforme
                      </label>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className={[
              'mt-3 px-3 py-2.5 rounded-lg text-xs border',
              todosConformes
                ? 'bg-emerald-50 border-emerald-200 text-emerald-700'
                : 'bg-amber-50 border-amber-200 text-amber-700',
            ].join(' ')}>
              {todosConformes
                ? 'Todos los controles conformes (RN-E5-006). Listo para registrar la Decisión de Empleo.'
                : 'Aún faltan controles por confirmar, o la cadena de frío está fuera de rango.'}
            </div>
          </div>

          {mostrarRechazo && (
            <div>
              <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">Motivo del rechazo (RN-E5-007)</p>
              <textarea
                value={motivoRechazo}
                onChange={(e) => setMotivoRechazo(e.target.value)}
                placeholder="Describe el motivo del rechazo del lote..."
                rows={3}
                className="w-full border border-slate-200 bg-white hover:border-slate-300 rounded-lg px-3.5 py-2.5 text-sm text-slate-800 placeholder:text-slate-400 outline-none transition-all duration-150 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500"
              />
            </div>
          )}

          <div className="flex justify-end gap-3">
            <Button variant="danger" onClick={handleRechazar} loading={procesando}>
              {mostrarRechazo ? 'Confirmar rechazo — Devolución' : 'Rechazar lote — Devolución'}
            </Button>
            <Button onClick={handleAprobar} loading={procesando} disabled={!todosConformes}>
              Aprobar lote — Libre utilización
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Inspecciones QA (listado)
// ─────────────────────────────────────────────────────────────────────────
function InspeccionesQATab() {
  const { inspecciones, loading, error } = useInspeccionCalidad();
  const [detalleRow, setDetalleRow] = useState(null);

  const COLUMNS = [
    { key: 'numero', label: 'N° Inspección' },
    { key: 'numeroEntradaMercancia', label: 'Entrada MIGO' },
    { key: 'lote', label: 'Lote' },
    { key: 'decision', label: 'Decisión', render: (val) => <Badge value={DECISION_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver detalle</button>
      ) },
  ];

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{inspecciones.length} inspecciones de calidad registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando inspecciones de calidad...</div>
        ) : (
          <Table columns={COLUMNS} data={inspecciones} />
        )}
      </div>

      <Modal isOpen={!!detalleRow} title={`Detalle — ${detalleRow?.numero ?? ''}`} onClose={() => setDetalleRow(null)}>
        {detalleRow && (
          <div className="flex flex-col gap-2 text-sm">
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Entrada MIGO</span>
              <b className="font-mono">{detalleRow.numeroEntradaMercancia}</b>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Lote</span>
              <span className="font-mono">{detalleRow.lote}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Muestreo técnico</span>
              <Badge value={detalleRow.muestreoConforme ? 'ACTIVO' : 'INACTIVO'} />
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Cadena de frío</span>
              <Badge value={detalleRow.cadenaFrioConforme ? 'ACTIVO' : 'INACTIVO'} />
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Registro sanitario</span>
              <Badge value={detalleRow.registroSanitarioVigente ? 'ACTIVO' : 'INACTIVO'} />
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Empaque</span>
              <Badge value={detalleRow.empaqueConforme ? 'ACTIVO' : 'INACTIVO'} />
            </div>
            {detalleRow.rechazado && detalleRow.motivoRechazo && (
              <div className="px-3 py-2 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">
                <span className="font-mono font-semibold mr-1">RN-E5-007</span>
                {detalleRow.motivoRechazo}
              </div>
            )}
            <div className="mt-1 px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
              Decisión: {DECISION_LABEL[detalleRow.decision] ?? detalleRow.decision}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 05: Inspección y aseguramiento de calidad (QA11)
// ─────────────────────────────────────────────────────────────────────────
export default function InspeccionCalidadPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('inspeccionar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={4} maxReached={5} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-E5-001 · RN-E5-005 · RN-E5-006 · LOG.06"
          title="Inspección y aseguramiento de calidad (QA11)"
          description="El Regente Farmacéutico ejecuta el muestreo técnico, valida la cadena de frío, el registro sanitario y el empaque antes de emitir la Decisión de Empleo del lote."
          badge="QA11"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'inspeccionar', label: 'Inspeccionar lote' },
            { id: 'inspecciones', label: 'Inspecciones QA' },
          ].map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={[
                'px-4 py-2.5 text-sm font-medium rounded-t-lg border-b-2 -mb-px transition-colors',
                tab === t.id
                  ? 'border-teal-700 text-teal-800'
                  : 'border-transparent text-slate-500 hover:text-slate-700',
              ].join(' ')}
            >
              {t.label}
            </button>
          ))}
        </div>

        {tab === 'inspeccionar'
          ? <InspeccionarLoteTab key={refreshKey} onProcesado={() => { setRefreshKey((k) => k + 1); setTab('inspecciones'); }} />
          : <InspeccionesQATab key={`qa-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 05 de {BLOQUE_A_FASES.length} — el lote aprobado queda en Libre Utilización, listo para la distribución (Fase 06)
      </p>
    </div>
  );
}
