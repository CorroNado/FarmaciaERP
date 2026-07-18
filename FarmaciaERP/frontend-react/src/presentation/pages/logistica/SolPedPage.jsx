import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Table    from '@/presentation/components/ui/Table';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import Modal    from '@/presentation/components/ui/Modal';
import LogisticaRail, { BLOQUE_A_FASES } from '@/presentation/components/logistica/LogisticaRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useSolPed }      from '@/presentation/hooks/Logistica/useSolPed';
import { useProveedores } from '@/presentation/hooks/Logistica/useProveedores';
import { useConvenios }   from '@/presentation/hooks/Ventas/useConvenios';
import { useCases }       from '@/infrastructure';

const ESTADO_BADGE = {
  LIBERADA: 'USUARIO',
  FUENTE_APROBADA: 'ADMINISTRADOR',
  CONVERTIDA_OC: 'ACTIVO',
  RECHAZADA: 'INACTIVO',
};

function emptyLinea() { return { medicamentoId: '', stockMinimo: '' }; }
const INITIAL_HEADER = { responsable: '', centroCosto: '', presupuesto: '' };

// ─────────────────────────────────────────────────────────────────────────
// TAB: Nueva SolPed (LOG.01 — Planificación de necesidades / MRP)
// ─────────────────────────────────────────────────────────────────────────
function NuevaSolPedTab({ onCreated }) {
  const { sugerencia, calculando, loading, error, calcularMRP, clearSugerencia, crearSolPed } = useSolPed();
  const [medicamentos, setMedicamentos] = useState([]);
  const [header, setHeader] = useState(INITIAL_HEADER);
  const [lineas, setLineas] = useState([emptyLinea()]);
  const [localError, setLocalError] = useState('');
  const [creando, setCreando] = useState(false);

  useEffect(() => {
    useCases.medicamentos.getAll.execute({}).then(setMedicamentos).catch(() => setMedicamentos([]));
  }, []);

  const medicamentoOptions = medicamentos.map((m) => ({ value: m.id, label: m.nombre }));

  function handleHeaderChange(e) {
    setHeader((p) => ({ ...p, [e.target.name]: e.target.value }));
  }
  function handleLineaChange(idx, field, value) {
    setLineas((prev) => {
      const next = [...prev];
      next[idx] = { ...next[idx], [field]: value };
      return next;
    });
  }
  function addLinea() {
    setLineas((prev) => [...prev, emptyLinea()]);
  }
  function removeLinea(idx) {
    setLineas((prev) => prev.filter((_, i) => i !== idx));
  }

  async function handleCalcular() {
    setLocalError('');
    const data = await calcularMRP(lineas);
    if (!data) setLocalError('No se pudo calcular la sugerencia MRP');
  }

  function handleCantidadSugeridaChange(idx, value) {
    // La sugerencia se edita localmente antes de confirmar la SolPed
    const next = [...sugerencia];
    next[idx] = { ...next[idx], cantidadSugerida: Number(value) };
    // truco: reescribimos el arreglo vía calcularMRP no aplica; usamos estado local espejo
    setSugerenciaEditable(next);
  }

  // Espejo editable de la sugerencia (para permitir ajustar cantidades antes de emitir la SolPed)
  const [sugerenciaEditable, setSugerenciaEditable] = useState([]);
  useEffect(() => { setSugerenciaEditable(sugerencia); }, [sugerencia]);

  const total = sugerenciaEditable.reduce((s, i) => s + (i.cantidadSugerida * i.precioUnitario), 0);
  const presupuestoNum = Number(header.presupuesto) || 0;
  const saldo = presupuestoNum - total;

  const COLUMNS = [
    { key: 'nombreMedicamento', label: 'Medicamento' },
    { key: 'stockActual', label: 'Stock actual' },
    { key: 'stockMinimo', label: 'Stock mínimo' },
    { key: 'cantidadSugerida', label: 'Cant. a solicitar', render: (val, row) => {
        const idx = sugerenciaEditable.indexOf(row);
        return (
          <input
            type="number"
            min="1"
            value={val}
            onChange={(e) => handleCantidadSugeridaChange(idx, e.target.value)}
            className="w-24 border border-slate-200 rounded-lg px-2 py-1 text-sm focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none"
          />
        );
      } },
    { key: 'precioUnitario', label: 'Precio unit.', render: (val) => `S/ ${val.toFixed(2)}` },
    { key: 'porDebajoDelMinimo', label: 'RN-E1-002', render: (val) => (
        val
          ? <span className="text-[11px] font-mono px-2 py-0.5 rounded-full bg-red-50 text-red-600 border border-red-200">bajo mínimo</span>
          : <span className="text-[11px] font-mono px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200">ok</span>
      ) },
  ];

  async function handleGenerarSolPed() {
    setLocalError('');
    if (!sugerenciaEditable.length) {
      setLocalError('Calcula primero la sugerencia MRP');
      return;
    }
    setCreando(true);
    const ok = await crearSolPed({
      ...header,
      detalles: sugerenciaEditable.map((i) => ({
        medicamentoId: i.medicamentoId,
        stockMinimo: i.stockMinimo,
        cantidadSugerida: i.cantidadSugerida,
      })),
    });
    setCreando(false);
    if (ok) {
      setHeader(INITIAL_HEADER);
      setLineas([emptyLinea()]);
      clearSugerencia();
      setSugerenciaEditable([]);
      onCreated?.();
    } else {
      setLocalError(error ?? 'No se pudo generar la SolPed');
    }
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN-E1-002</span>
        La necesidad de abastecimiento surge cuando el stock actual está por debajo del stock mínimo de seguridad.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">Cabecera de la SolPed</p>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <Input label="Responsable" name="responsable" value={header.responsable} onChange={handleHeaderChange} placeholder="Category Manager" />
          <Input label="Centro de costo" name="centroCosto" value={header.centroCosto} onChange={handleHeaderChange} placeholder="CC-COMPRAS-001" />
          <Input label="Presupuesto disponible (S/)" name="presupuesto" type="number" value={header.presupuesto} onChange={handleHeaderChange} />
        </div>
      </div>

      <div>
        <div className="flex items-center justify-between mb-2">
          <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide">1. Selección de medicamentos para el MRP</p>
          <button type="button" onClick={addLinea} className="text-xs text-teal-700 hover:underline font-medium">+ Agregar línea</button>
        </div>
        <div className="flex flex-col gap-3">
          {lineas.map((linea, idx) => (
            <div key={idx} className="grid grid-cols-[1fr_140px_auto] gap-2 items-end">
              <Select
                name={`med-${idx}`}
                placeholder="Medicamento..."
                value={linea.medicamentoId}
                onChange={(e) => handleLineaChange(idx, 'medicamentoId', e.target.value)}
                options={medicamentoOptions}
              />
              <Input
                name={`min-${idx}`}
                type="number"
                placeholder="Stock mínimo"
                value={linea.stockMinimo}
                onChange={(e) => handleLineaChange(idx, 'stockMinimo', e.target.value)}
              />
              <button
                type="button"
                onClick={() => removeLinea(idx)}
                disabled={lineas.length === 1}
                className="h-[42px] w-9 flex items-center justify-center text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all disabled:opacity-30 disabled:cursor-not-allowed"
              >
                ✕
              </button>
            </div>
          ))}
        </div>
        <div className="mt-3">
          <Button variant="outline" onClick={handleCalcular} loading={calculando}>
            Calcular sugerencia MRP →
          </Button>
        </div>
      </div>

      {sugerenciaEditable.length > 0 && (
        <div>
          <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Sugerencia MRP — ajusta cantidades y confirma</p>
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
            <Table columns={COLUMNS} data={sugerenciaEditable.map((s, i) => ({ ...s, id: i }))} />
          </div>

          <div className="mt-4 bg-slate-50 rounded-xl border border-slate-200 p-4 flex flex-col gap-1.5 max-w-sm ml-auto">
            <div className="flex justify-between text-sm"><span className="text-slate-500">Monto total solicitado</span><b className="font-mono">S/ {total.toFixed(2)}</b></div>
            <div className="flex justify-between text-sm"><span className="text-slate-500">Presupuesto disponible</span><b className="font-mono">S/ {presupuestoNum.toFixed(2)}</b></div>
            <div className={`flex justify-between text-sm ${saldo < 0 ? 'text-red-600' : 'text-emerald-700'}`}>
              <span>Saldo resultante</span><b className="font-mono">S/ {saldo.toFixed(2)}</b>
            </div>
          </div>

          <div className="mt-4 flex justify-end">
            <Button onClick={handleGenerarSolPed} loading={creando || loading} disabled={!header.responsable || !header.centroCosto || !header.presupuesto}>
              Generar Solicitud de Pedido
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Solicitudes (listado + aprobar fuente / rechazar)
// ─────────────────────────────────────────────────────────────────────────
function SolicitudesTab() {
  const { solpeds, loading, error, aprobarFuente, rechazarSolPed } = useSolPed();
  const { proveedores } = useProveedores();
  const { convenios } = useConvenios();

  const [detalleRow, setDetalleRow] = useState(null);
  const [aprobarRow, setAprobarRow] = useState(null);
  const [aprobarForm, setAprobarForm] = useState({ proveedorId: '', convenioId: '' });
  const [aprobando, setAprobando] = useState(false);
  const [aprobarError, setAprobarError] = useState('');

  const [rechazarRow, setRechazarRow] = useState(null);
  const [motivo, setMotivo] = useState('');
  const [rechazando, setRechazando] = useState(false);

  const proveedorOptions = proveedores.filter((p) => p.estaActivo).map((p) => ({ value: p.id, label: p.razonSocial }));
  const convenioOptions = convenios
    .filter((c) => c.vigente && (!aprobarForm.proveedorId || String(c.proveedorId) === String(aprobarForm.proveedorId)))
    .map((c) => ({ value: c.id, label: `${c.numero} — ${c.razonSocialProveedor}` }));

  const COLUMNS = [
    { key: 'numero', label: 'N° SolPed' },
    { key: 'responsable', label: 'Responsable' },
    { key: 'centroCosto', label: 'Centro costo' },
    { key: 'total', label: 'Monto', render: (val) => `S/ ${val.toFixed(2)}` },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={ESTADO_BADGE[val] ?? val} /> },
    { key: 'razonSocialProveedor', label: 'Proveedor', render: (val) => val || '—' },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver ítems</button>
      ) },
  ];

  function openAprobar(row) {
    setAprobarRow(row);
    setAprobarForm({ proveedorId: '', convenioId: '' });
    setAprobarError('');
  }
  async function handleConfirmarAprobar() {
    setAprobarError('');
    setAprobando(true);
    const ok = await aprobarFuente(aprobarRow.id, aprobarForm);
    setAprobando(false);
    if (ok) setAprobarRow(null);
    else setAprobarError(error ?? 'No se pudo aprobar la fuente de aprovisionamiento');
  }

  function openRechazar(row) {
    setRechazarRow(row);
    setMotivo('');
  }
  async function handleConfirmarRechazar() {
    setRechazando(true);
    const ok = await rechazarSolPed(rechazarRow.id, motivo);
    setRechazando(false);
    if (ok) setRechazarRow(null);
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{solpeds.length} solicitudes de pedido registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando solicitudes...</div>
        ) : (
          <div className="overflow-x-auto">
            <Table columns={COLUMNS} data={solpeds} />
            {solpeds.length > 0 && (
              <div className="px-4 py-3 border-t border-slate-100 flex flex-col gap-2">
                {solpeds.filter((s) => s.puedeAprobarFuente || s.puedeRechazar).map((s) => (
                  <div key={s.id} className="flex items-center justify-between text-xs bg-slate-50 rounded-lg px-3 py-2">
                    <span className="font-mono text-slate-500">{s.numero}</span>
                    <div className="flex gap-2">
                      {s.puedeAprobarFuente && (
                        <button onClick={() => openAprobar(s)} className="px-2.5 py-1 rounded-lg bg-teal-700 text-white hover:bg-teal-800 transition-colors">
                          Aprobar fuente
                        </button>
                      )}
                      {s.puedeRechazar && (
                        <button onClick={() => openRechazar(s)} className="px-2.5 py-1 rounded-lg border border-red-200 text-red-600 hover:bg-red-50 transition-colors">
                          Rechazar
                        </button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>

      {/* Detalle de ítems */}
      <Modal isOpen={!!detalleRow} title={`Detalle — ${detalleRow?.numero ?? ''}`} onClose={() => setDetalleRow(null)}>
        <div className="flex flex-col gap-2 max-h-[50vh] overflow-y-auto">
          {detalleRow?.detalles.map((d, i) => (
            <div key={i} className="flex justify-between text-sm border-b border-slate-100 pb-2">
              <span>{d.nombreMedicamento} <span className="text-slate-400">× {d.cantidadSugerida}</span></span>
              <b className="font-mono">S/ {d.subtotal.toFixed(2)}</b>
            </div>
          ))}
          {detalleRow?.motivoRechazo && (
            <div className="mt-2 px-3 py-2 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">
              Motivo de rechazo: {detalleRow.motivoRechazo}
            </div>
          )}
        </div>
      </Modal>

      {/* Aprobar fuente */}
      <Modal
        isOpen={!!aprobarRow}
        title={`Aprobar fuente de aprovisionamiento — ${aprobarRow?.numero ?? ''}`}
        onClose={() => setAprobarRow(null)}
        onConfirm={handleConfirmarAprobar}
        confirmText="Aprobar fuente"
        loading={aprobando}
      >
        <div className="flex flex-col gap-4">
          <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
            <span className="font-mono font-semibold mr-1">RN-MM-001</span>
            El convenio debe estar vigente para respaldar la fuente de aprovisionamiento.
          </div>
          {aprobarError && (
            <div className="px-3 py-2.5 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">{aprobarError}</div>
          )}
          <Select
            label="Proveedor homologado"
            name="proveedorId"
            value={aprobarForm.proveedorId}
            onChange={(e) => setAprobarForm((p) => ({ ...p, proveedorId: e.target.value, convenioId: '' }))}
            options={proveedorOptions}
          />
          <Select
            label="Convenio marco vigente"
            name="convenioId"
            value={aprobarForm.convenioId}
            onChange={(e) => setAprobarForm((p) => ({ ...p, convenioId: e.target.value }))}
            options={convenioOptions}
            disabled={!aprobarForm.proveedorId}
          />
          {aprobarForm.proveedorId && convenioOptions.length === 0 && (
            <p className="text-xs text-amber-700">Este proveedor no tiene convenios vigentes.</p>
          )}
        </div>
      </Modal>

      {/* Rechazar */}
      <Modal
        isOpen={!!rechazarRow}
        title={`Rechazar SolPed — ${rechazarRow?.numero ?? ''}`}
        onClose={() => setRechazarRow(null)}
        onConfirm={handleConfirmarRechazar}
        confirmText="Rechazar"
        confirmVariant="danger"
        loading={rechazando}
      >
        <div className="flex flex-col gap-3">
          <label className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Motivo del rechazo</label>
          <textarea
            value={motivo}
            onChange={(e) => setMotivo(e.target.value)}
            rows={3}
            className="w-full border border-slate-200 rounded-lg px-3.5 py-2.5 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500"
            placeholder="Explica el motivo del rechazo..."
          />
        </div>
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 02: Planificación de necesidades (MRP) / SolPed
// ─────────────────────────────────────────────────────────────────────────
export default function SolPedPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('nueva');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={1} maxReached={3} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-E1-001 · RN-E1-002 · RN-E1-006 · LOG.01"
          title="Planificación de necesidades y Solicitud de Pedido"
          description="Calcula la sugerencia de reabastecimiento (MRP) a partir del stock mínimo, genera la SolPed y aprueba la fuente de aprovisionamiento contra un convenio marco vigente."
          badge="MD04 / ME51N"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'nueva', label: 'Nueva SolPed (MRP)' },
            { id: 'solicitudes', label: 'Solicitudes' },
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

        {tab === 'nueva'
          ? <NuevaSolPedTab key={refreshKey} onCreated={() => { setRefreshKey((k) => k + 1); setTab('solicitudes'); }} />
          : <SolicitudesTab key={`sol-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 02 de {BLOQUE_A_FASES.length} — SolPed liberada al generar; aprueba la fuente para habilitar la Orden de Compra
      </p>
    </div>
  );
}
