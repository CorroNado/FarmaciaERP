import { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import Table    from '@/presentation/components/ui/Table';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import Modal    from '@/presentation/components/ui/Modal';
import LogisticaRail, { BLOQUE_A_FASES } from '@/presentation/components/logistica/LogisticaRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useOrdenCompra } from '@/presentation/hooks/useOrdenCompra';
import { useSolPed }      from '@/presentation/hooks/useSolPed';

const ESTADO_BADGE = {
  BORRADOR: 'ADMINISTRADOR',
  FIRMADA:  'ACTIVO',
  ANULADA:  'INACTIVO',
};

const ESTADO_LABEL = {
  BORRADOR: 'Borrador',
  FIRMADA:  'Firmada y despachada',
  ANULADA:  'Anulada',
};

const CENTRO_DESTINO_DEFAULT = 'CE01 — Almacén Central Lima';

// ─────────────────────────────────────────────────────────────────────────
// TAB: Generar OC (LOG.04 — a partir de una SolPed con fuente aprobada)
// ─────────────────────────────────────────────────────────────────────────
function GenerarOCTab({ onCreated }) {
  const { solpeds, loading: loadingSolPeds } = useSolPed();
  const { crearOrdenCompra, creando, error } = useOrdenCompra();

  const [solPedId, setSolPedId] = useState('');
  const [centroDestino, setCentroDestino] = useState(CENTRO_DESTINO_DEFAULT);
  const [localError, setLocalError] = useState('');

  // RN-OC-001: solo procede si la SolPed ya tiene la fuente de aprovisionamiento aprobada
  const solpedsElegibles = useMemo(
    () => solpeds.filter((s) => s.estado === 'FUENTE_APROBADA'),
    [solpeds]
  );
  const solPedOptions = solpedsElegibles.map((s) => ({
    value: s.id,
    label: `${s.numero} — ${s.razonSocialProveedor} — S/ ${s.total.toFixed(2)}`,
  }));

  const solPedSeleccionada = solpedsElegibles.find((s) => String(s.id) === String(solPedId));

  async function handleGenerar() {
    setLocalError('');
    if (!solPedSeleccionada) {
      setLocalError('Selecciona una SolPed con fuente de aprovisionamiento aprobada');
      return;
    }
    const oc = await crearOrdenCompra({ solPedId: solPedSeleccionada.id, centroDestino });
    if (oc) {
      setSolPedId('');
      setCentroDestino(CENTRO_DESTINO_DEFAULT);
      onCreated?.();
    } else {
      setLocalError(error ?? 'No se pudo generar la Orden de Compra');
    }
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN-OC-002</span>
        Precio y proveedor quedan bloqueados: se heredan del convenio marco / Info-Record aprobado en la Fase 02.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. SolPed con fuente aprobada</p>
        <Select
          name="solPedId"
          placeholder={loadingSolPeds ? 'Cargando solicitudes...' : 'Selecciona una SolPed...'}
          value={solPedId}
          onChange={(e) => setSolPedId(e.target.value)}
          options={solPedOptions}
          disabled={loadingSolPeds}
        />
        {!loadingSolPeds && solPedOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay SolPed con fuente de aprovisionamiento aprobada. Vuelve a la Fase 02 para aprobar una.
          </p>
        )}
      </div>

      {solPedSeleccionada && (
        <div>
          <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Datos heredados (no editables)</p>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <Input label="Proveedor" name="proveedor" value={solPedSeleccionada.razonSocialProveedor} disabled onChange={() => {}} />
            <Input label="Convenio marco" name="convenio" value={solPedSeleccionada.numeroConvenio} disabled onChange={() => {}} />
            <Input label="Monto total (S/)" name="monto" value={solPedSeleccionada.total.toFixed(2)} disabled onChange={() => {}} />
          </div>

          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-4">
            <Table
              columns={[
                { key: 'nombreMedicamento', label: 'Medicamento' },
                { key: 'cantidadSugerida', label: 'Cantidad' },
                { key: 'precioUnitario', label: 'Precio unit.', render: (val) => `S/ ${val.toFixed(2)}` },
                { key: 'subtotal', label: 'Subtotal', render: (val) => `S/ ${val.toFixed(2)}` },
              ]}
              data={solPedSeleccionada.detalles.map((d, i) => ({ ...d, id: i }))}
            />
          </div>

          <div className="mt-4">
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">3. Centro de destino (RN-OC-003)</p>
            <Input
              name="centroDestino"
              label="Muelle / Centro de destino"
              value={centroDestino}
              onChange={(e) => setCentroDestino(e.target.value)}
              placeholder="CE01 — Almacén Central Lima"
            />
          </div>

          <div className="mt-4 flex justify-end">
            <Button onClick={handleGenerar} loading={creando} disabled={!centroDestino.trim()}>
              Generar Orden de Compra (borrador)
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Órdenes de Compra (listado + firma digital y liberación)
// ─────────────────────────────────────────────────────────────────────────
function OrdenesCompraTab() {
  const { ordenes, loading, error, firmarOrdenCompra } = useOrdenCompra();

  const [detalleRow, setDetalleRow] = useState(null);
  const [firmarRow, setFirmarRow] = useState(null);
  const [fechaEntrega, setFechaEntrega] = useState('');
  const [firmando, setFirmando] = useState(false);
  const [firmarError, setFirmarError] = useState('');

  const COLUMNS = [
    { key: 'numero', label: 'N° Orden de Compra' },
    { key: 'numeroSolPed', label: 'SolPed origen' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'centroDestino', label: 'Centro destino' },
    { key: 'montoTotal', label: 'Monto', render: (val) => `S/ ${val.toFixed(2)}` },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={ESTADO_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver ítems</button>
      ) },
  ];

  function openFirmar(row) {
    setFirmarRow(row);
    setFechaEntrega('');
    setFirmarError('');
  }
  async function handleConfirmarFirmar() {
    if (!fechaEntrega.trim()) {
      setFirmarError('RN-OC-003: la fecha límite de entrega es obligatoria');
      return;
    }
    setFirmarError('');
    setFirmando(true);
    const ok = await firmarOrdenCompra(firmarRow.id, fechaEntrega);
    setFirmando(false);
    if (ok) setFirmarRow(null);
    else setFirmarError(error ?? 'No se pudo firmar la Orden de Compra');
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{ordenes.length} órdenes de compra registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando órdenes de compra...</div>
        ) : (
          <div className="overflow-x-auto">
            <Table columns={COLUMNS} data={ordenes} />
            {ordenes.length > 0 && (
              <div className="px-4 py-3 border-t border-slate-100 flex flex-col gap-2">
                {ordenes.filter((o) => o.puedeFirmar).map((o) => (
                  <div key={o.id} className="flex items-center justify-between text-xs bg-slate-50 rounded-lg px-3 py-2">
                    <span className="font-mono text-slate-500">{o.numero}</span>
                    <button onClick={() => openFirmar(o)} className="px-2.5 py-1 rounded-lg bg-teal-700 text-white hover:bg-teal-800 transition-colors">
                      Firma digital y liberación →
                    </button>
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
              <span>{d.nombreMedicamento} <span className="text-slate-400">× {d.cantidad}</span></span>
              <b className="font-mono">S/ {d.subtotal.toFixed(2)}</b>
            </div>
          ))}
          {detalleRow?.firmada && (
            <div className="mt-2 px-3 py-2 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-700">
              Estado: {ESTADO_LABEL[detalleRow.estado]} · entrega límite {detalleRow.fechaEntregaLimite}
            </div>
          )}
        </div>
      </Modal>

      {/* Firma digital y liberación */}
      <Modal
        isOpen={!!firmarRow}
        title={`Firma digital y liberación — ${firmarRow?.numero ?? ''}`}
        onClose={() => setFirmarRow(null)}
        onConfirm={handleConfirmarFirmar}
        confirmText="Firmar y liberar OC"
        loading={firmando}
      >
        <div className="flex flex-col gap-4">
          <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
            <span className="font-mono font-semibold mr-1">RN-OC-008</span>
            Al firmar, la Orden de Compra se despacha electrónicamente al proveedor y queda lista para la recepción (MIGO).
          </div>
          {firmarError && (
            <div className="px-3 py-2.5 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">{firmarError}</div>
          )}
          <Input
            label="Fecha límite de entrega (RN-OC-003)"
            name="fechaEntrega"
            type="text"
            placeholder="dd/mm/aaaa"
            value={fechaEntrega}
            onChange={(e) => setFechaEntrega(e.target.value)}
          />
        </div>
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 03: Tratamiento y emisión de la Orden de Compra
// ─────────────────────────────────────────────────────────────────────────
export default function OrdenCompraPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('generar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={2} maxReached={3} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-OC-002 · RN-OC-003 · RN-OC-006 · RN-OC-008 · LOG.04"
          title="Tratamiento y emisión de la Orden de Compra"
          description="Genera la Orden de Compra a partir de una SolPed con fuente de aprovisionamiento aprobada. Precio y proveedor quedan bloqueados (heredados del convenio); define el centro de destino y la fecha límite de entrega, y firma digitalmente para despachar la OC al proveedor."
          badge="ME21N"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'generar', label: 'Generar OC' },
            { id: 'ordenes', label: 'Órdenes de Compra' },
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

        {tab === 'generar'
          ? <GenerarOCTab key={refreshKey} onCreated={() => { setRefreshKey((k) => k + 1); setTab('ordenes'); }} />
          : <OrdenesCompraTab key={`oc-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 03 de {BLOQUE_A_FASES.length} — la OC se emite como borrador y se firma digitalmente para su despacho electrónico
      </p>
    </div>
  );
}
