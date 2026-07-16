import { useState, useEffect } from 'react';
import { RotateCcw, Plus, Minus, X, CheckCircle2 } from 'lucide-react';
import Button from '@/presentation/components/ui/Button';
import Select from '@/presentation/components/ui/Select';
import Modal from '@/presentation/components/ui/Modal';
import { useDevolucion } from '@/presentation/hooks/useDevolucion';
import { MOTIVOS_DEVOLUCION, ACCIONES_DEVOLUCION } from '@/domain/models/Devolucion';

const MOTIVO_LABEL = Object.fromEntries(MOTIVOS_DEVOLUCION.map((m) => [m.value, m.label]));
const ACCION_LABEL = Object.fromEntries(ACCIONES_DEVOLUCION.map((a) => [a.value, a.label]));

function CantidadInput({ id, cantidad, maxCantidad, establecerCantidad }) {
  const [val, setVal] = useState(cantidad.toString());

  useEffect(() => {
    setVal(cantidad.toString());
  }, [cantidad]);

  const handleChange = (e) => {
    const rawValue = e.target.value;
    if (rawValue === '' || /^\d+$/.test(rawValue)) {
      setVal(rawValue);
      const parsed = parseInt(rawValue, 10);
      if (!isNaN(parsed)) {
        establecerCantidad(id, maxCantidad, parsed);
      }
    }
  };

  const handleBlur = () => {
    const parsed = parseInt(val, 10);
    if (isNaN(parsed) || parsed < 0) {
      setVal('0');
      establecerCantidad(id, maxCantidad, 0);
    } else if (parsed > maxCantidad) {
      setVal(maxCantidad.toString());
      establecerCantidad(id, maxCantidad, maxCantidad);
    }
  };

  return (
    <input
      type="text"
      value={val}
      onChange={handleChange}
      onBlur={handleBlur}
      onKeyDown={(e) => { if (e.key === 'Enter') e.target.blur(); }}
      className="w-10 text-center text-sm font-semibold text-slate-800 border border-slate-200 rounded-md py-0.5 outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
    />
  );
}

export default function DevolucionTab() {
  const {
    ventas, loadingVentas, cargarVentas,
    historial, loadingHistorial, cargarHistorial,
    ventaSeleccionada, seleccionarVenta,
    cantidades, cambiarCantidad, establecerCantidad,
    items, montoEstimado,
    motivo, setMotivo,
    accion, setAccion,
    enviando, registrarDevolucion,
    ultimaDevolucion, cerrarTicket,
    error, setError,
  } = useDevolucion();

  useEffect(() => {
    cargarVentas();
    cargarHistorial();
  }, [cargarVentas, cargarHistorial]);

  const puedeRegistrar = !!ventaSeleccionada && items.length > 0 && !!motivo && !!accion && !enviando;

  return (
    <div className="max-w-[1400px] mx-auto">
      {error && (
        <div className="mb-4 flex items-start justify-between gap-4 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl px-4 py-3">
          <span>{error}</span>
          <button onClick={() => setError(null)} className="text-red-400 hover:text-red-600">
            <X size={16} />
          </button>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-[1.4fr_1fr] gap-6 items-start">
        {/* ─── Solicitud de devolución ──────────────────────────── */}
        <div className="bg-white border border-slate-200 rounded-2xl p-6">
          <h2 className="text-lg font-bold text-slate-800 mb-1">Solicitud de devolución</h2>
          <p className="text-xs text-slate-500 mb-4">SD.07.01 · Crea una solicitud de devolución con motivo técnico sobre una venta ya cobrada.</p>

          <Select
            label="Venta de origen"
            name="ventaOrigen"
            value={ventaSeleccionada?.id ?? ''}
            onChange={(e) => seleccionarVenta(e.target.value)}
            placeholder={loadingVentas ? 'Cargando ventas...' : 'Selecciona una venta...'}
            disabled={loadingVentas}
            options={ventas.map((v) => ({
              value: v.id,
              label: `Venta #${v.id} · ${v.nombreCliente} · S/ ${v.total.toFixed(2)}`,
            }))}
          />

          {ventaSeleccionada && (
            <>
              <h4 className="text-xs font-semibold text-slate-600 uppercase tracking-wide mt-5 mb-2">
                Selecciona ítems y cantidad a devolver
              </h4>
              <div className="flex flex-col divide-y divide-slate-100">
                {(ventaSeleccionada.detalles ?? []).map((d) => {
                  const cantidadSel = cantidades[d.medicamentoId] ?? 0;
                  return (
                    <div key={d.medicamentoId} className="flex items-center justify-between py-3 gap-3">
                      <div className="min-w-0">
                        <div className="text-sm font-semibold text-slate-800 truncate">{d.nombreMedicamento}</div>
                        <div className="text-xs text-slate-400 font-mono">Cant. vendida: {d.cantidad}</div>
                      </div>
                      <div className="flex items-center gap-2 shrink-0">
                        <button
                          onClick={() => cambiarCantidad(d.medicamentoId, d.cantidad, -1)}
                          className="w-6 h-6 rounded-md border border-slate-200 flex items-center justify-center hover:bg-slate-50"
                        >
                          <Minus size={12} />
                        </button>
                        <CantidadInput id={d.medicamentoId} cantidad={cantidadSel} maxCantidad={d.cantidad} establecerCantidad={establecerCantidad} />
                        <button
                          onClick={() => cambiarCantidad(d.medicamentoId, d.cantidad, 1)}
                          disabled={cantidadSel >= d.cantidad}
                          className="w-6 h-6 rounded-md border border-slate-200 flex items-center justify-center hover:bg-slate-50 disabled:opacity-40"
                        >
                          <Plus size={12} />
                        </button>
                      </div>
                    </div>
                  );
                })}
              </div>

              <div className="grid grid-cols-2 gap-3 mt-5">
                <Select
                  label="Motivo de devolución"
                  name="motivo"
                  value={motivo}
                  onChange={(e) => setMotivo(e.target.value)}
                  options={MOTIVOS_DEVOLUCION}
                />
                <Select
                  label="Acción · SD.07.03"
                  name="accion"
                  value={accion}
                  onChange={(e) => setAccion(e.target.value)}
                  options={ACCIONES_DEVOLUCION}
                />
              </div>
            </>
          )}

          {!ventaSeleccionada && (
            <div className="text-center text-sm text-slate-400 py-10">
              Selecciona una venta para elegir los ítems a devolver.
            </div>
          )}

          <div className="mt-5 pt-4 border-t border-slate-100 flex justify-between text-base font-bold text-slate-900">
            <span>Monto estimado a favor del cliente</span><span>S/ {montoEstimado.toFixed(2)}</span>
          </div>

          <Button
            className="w-full mt-4 py-3 text-sm"
            disabled={!puedeRegistrar}
            loading={enviando}
            onClick={registrarDevolucion}
          >
            Registrar devolución
          </Button>
        </div>

        {/* ─── Historial ─────────────────────────────────────────── */}
        <div className="bg-white border border-slate-200 rounded-2xl p-6">
          <h2 className="text-lg font-bold text-slate-800 mb-1 flex items-center gap-2">
            <RotateCcw size={18} /> Historial de devoluciones
          </h2>
          <p className="text-xs text-slate-500 mb-4">SD.07.02 · Entrega inversa y actualización de stock.</p>

          {loadingHistorial && (
            <div className="text-center text-sm text-slate-400 py-6">Cargando historial...</div>
          )}
          {!loadingHistorial && historial.length === 0 && (
            <div className="text-center text-sm text-slate-400 py-6">Aún no hay devoluciones registradas.</div>
          )}

          <div className="flex flex-col gap-3 max-h-[600px] overflow-y-auto pr-1 scrollbar-hide">
            {historial.map((d) => {
              const itemsTxt = d.detalles.map((i) => `${i.cantidad}× ${i.nombreMedicamento}`).join(', ');
              return (
                <div key={d.id} className="border border-slate-200 rounded-xl px-4 py-3">
                  <div className="flex items-center justify-between gap-2 mb-1">
                    <span className="text-sm font-semibold text-slate-800">DEV-{d.id}</span>
                    <span className="inline-flex items-center gap-1 text-[11px] font-medium px-2 py-0.5 rounded-full bg-blue-50 text-blue-700 border border-blue-200">
                      {ACCION_LABEL[d.accion] ?? d.accion}
                    </span>
                    <span className="text-sm font-semibold text-slate-800 ml-auto">S/ {d.monto.toFixed(2)}</span>
                  </div>
                  <div className="text-xs text-slate-500">{itemsTxt}</div>
                  <div className="text-xs text-slate-400 mt-0.5">
                    Origen venta #{d.ventaId} · Motivo: {MOTIVO_LABEL[d.motivo] ?? d.motivo}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* Modal: devolución registrada */}
      {ultimaDevolucion && (
        <Modal
          isOpen={!!ultimaDevolucion}
          title="Devolución registrada"
          onClose={cerrarTicket}
          onConfirm={cerrarTicket}
          confirmText="Cerrar"
        >
          <div className="flex flex-col items-center text-center gap-2 mb-4">
            <CheckCircle2 className="text-emerald-500" size={40} />
            <div className="text-sm text-slate-500">DEV-{ultimaDevolucion.id} · Sobre venta #{ultimaDevolucion.ventaId}</div>
          </div>
          <div className="flex flex-col divide-y divide-slate-100 text-sm mb-3">
            {ultimaDevolucion.detalles.map((d) => (
              <div key={d.medicamentoId} className="flex justify-between py-1.5">
                <span>{d.cantidad}× {d.nombreMedicamento}</span>
                <span>S/ {d.subtotal.toFixed(2)}</span>
              </div>
            ))}
          </div>
          <div className="flex flex-col gap-1 text-xs text-slate-500 mb-3">
            <div className="flex justify-between"><span>Motivo</span><span>{MOTIVO_LABEL[ultimaDevolucion.motivo] ?? ultimaDevolucion.motivo}</span></div>
            <div className="flex justify-between"><span>Acción</span><span>{ACCION_LABEL[ultimaDevolucion.accion] ?? ultimaDevolucion.accion}</span></div>
          </div>
          <div className="flex justify-between text-base font-bold text-slate-900 border-t border-slate-100 pt-3">
            <span>Monto a favor del cliente</span><span>S/ {ultimaDevolucion.monto.toFixed(2)}</span>
          </div>
        </Modal>
      )}
    </div>
  );
}
