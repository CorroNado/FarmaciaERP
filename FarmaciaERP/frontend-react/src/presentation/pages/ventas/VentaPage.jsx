import { useState } from 'react';
import { Search, Plus, Minus, X, ShoppingCart, CreditCard, Wallet, Banknote, CheckCircle2, FileText, RotateCcw } from 'lucide-react';
import Button from '@/presentation/components/ui/Button';
import Select from '@/presentation/components/ui/Select';
import Modal from '@/presentation/components/ui/Modal';
import ClienteSelector from '@/presentation/components/ventas/ClienteSelector';
import NuevoClienteModal from '@/presentation/components/ventas/NuevoClienteModal';
import CotizacionTab from '@/presentation/pages/ventas/CotizacionTab';
import DevolucionTab from '@/presentation/pages/ventas/DevolucionTab';
import { useVenta } from '@/presentation/hooks/useVenta';

const METODOS_PAGO = [
  { value: 'EFECTIVO', label: 'Efectivo', icon: Banknote },
  { value: 'TARJETA', label: 'Tarjeta', icon: CreditCard },
  { value: 'YAPE', label: 'Yape / Plin', icon: Wallet },
];

const COMPROBANTES = [
  { value: 'BOLETA', label: 'Boleta de venta' },
  { value: 'FACTURA', label: 'Factura' },
  { value: 'NINGUNO', label: 'Sin comprobante' },
];

export default function VentaPage() {
  const {
    medicamentos, loadingCatalogo, error, setError, cargarCatalogo,
    cart, agregarAlCarrito, cambiarCantidad, quitarDelCarrito, totales,
    cliente, buscarClientePorDni, registrarClienteRapido, limpiarCliente,
    metodoPago, setMetodoPago, tipoComprobante, setTipoComprobante,
    procesando, procesarPago, ultimaVenta, cerrarTicket,
  } = useVenta();

  const [tab, setTab] = useState('venta'); // 'venta' | 'cotizacion' | 'devolucion'
  const [busqueda, setBusqueda] = useState('');
  const [dniInput, setDniInput] = useState('');
  const [mostrarNuevoCliente, setMostrarNuevoCliente] = useState(false);
  const [nuevoCliente, setNuevoCliente] = useState({ nombre: '', apellido: '', dni: '', tipoSeguro: 'SIN_SEGURO' });

  function onBuscar(value) {
    setBusqueda(value);
    cargarCatalogo(value);
  }

  async function onBuscarDni() {
    const encontrado = await buscarClientePorDni(dniInput);
    if (encontrado) setDniInput('');
  }

  async function onCrearClienteRapido() {
    const creado = await registrarClienteRapido(nuevoCliente);
    if (creado) {
      setMostrarNuevoCliente(false);
      setNuevoCliente({ nombre: '', apellido: '', dni: '', tipoSeguro: 'SIN_SEGURO' });
    }
  }

  const puedeCobrar = cart.length > 0 && !!cliente && !procesando;

  return (
    <div className="max-w-[1400px] mx-auto">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-slate-900">Ventas</h1>
        <p className="text-sm text-slate-500 mt-1">Registra una venta, cotiza para un cliente y da seguimiento a las ofertas enviadas.</p>
      </div>

      <div className="flex gap-1 mb-6 border-b border-slate-200">
        <button
          onClick={() => setTab('venta')}
          className={`flex items-center gap-2 px-4 py-2.5 text-sm font-semibold border-b-2 -mb-px transition ${
            tab === 'venta' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'
          }`}
        >
          <ShoppingCart size={15} /> Punto de venta
        </button>
        <button
          onClick={() => setTab('cotizacion')}
          className={`flex items-center gap-2 px-4 py-2.5 text-sm font-semibold border-b-2 -mb-px transition ${
            tab === 'cotizacion' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'
          }`}
        >
          <FileText size={15} /> Cotizaciones · SD.02
        </button>
        <button
          onClick={() => setTab('devolucion')}
          className={`flex items-center gap-2 px-4 py-2.5 text-sm font-semibold border-b-2 -mb-px transition ${
            tab === 'devolucion' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-500 hover:text-slate-700'
          }`}
        >
          <RotateCcw size={15} /> Devoluciones · SD.07
        </button>
      </div>

      {tab === 'cotizacion' && (
        <CotizacionTab
          medicamentos={medicamentos}
          loadingCatalogo={loadingCatalogo}
          cargarCatalogo={cargarCatalogo}
        />
      )}

      {tab === 'devolucion' && <DevolucionTab />}

      {tab === 'venta' && (
      <>
      {error && (
        <div className="mb-4 flex items-start justify-between gap-4 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl px-4 py-3">
          <span>{error}</span>
          <button onClick={() => setError(null)} className="text-red-400 hover:text-red-600">
            <X size={16} />
          </button>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-[1.4fr_1fr] gap-6 items-start">
        {/* ─── Catálogo ─────────────────────────────────────────── */}
        <div className="bg-white border border-slate-200 rounded-2xl p-6">
          <h2 className="text-lg font-bold text-slate-800 mb-1">Catálogo de medicamentos</h2>
          <p className="text-xs text-slate-500 mb-4">Busca por nombre y agrega productos al carrito.</p>

          <div className="relative mb-4">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              value={busqueda}
              onChange={(e) => onBuscar(e.target.value)}
              placeholder="Buscar medicamento por nombre..."
              className="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition text-sm"
            />
          </div>

          <div className="flex flex-col gap-2 max-h-[460px] overflow-y-auto pr-1 scrollbar-hide">
            {loadingCatalogo && (
              <div className="text-center text-sm text-slate-400 py-10">Cargando catálogo...</div>
            )}
            {!loadingCatalogo && medicamentos.length === 0 && (
              <div className="text-center text-sm text-slate-400 py-10">Sin resultados.</div>
            )}
            {!loadingCatalogo && medicamentos.map((m) => {
              const disabled = !m.estaDisponible || m.requiereReceta;
              return (
                <div key={m.id} className="flex items-center justify-between border border-slate-200 rounded-xl px-4 py-3 hover:border-blue-300 transition">
                  <div>
                    <div className="font-semibold text-sm text-slate-800">{m.nombre}</div>
                    <div className="text-xs text-slate-400 font-mono mt-0.5">
                      {m.presentacion || 'Presentación única'}
                      {m.requiereReceta && ' · Requiere receta'}
                    </div>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className={`text-[10px] font-mono px-2 py-0.5 rounded-full ${m.stockBajo ? 'bg-amber-50 text-amber-700' : 'bg-emerald-50 text-emerald-700'}`}>
                      {m.stock <= 0 ? 'Sin stock' : m.stockBajo ? `Stock bajo · ${m.stock}` : `Stock OK · ${m.stock}`}
                    </span>
                    <span className="font-mono font-semibold text-sm text-slate-800 w-20 text-right">S/ {m.precio.toFixed(2)}</span>
                    <button
                      disabled={disabled}
                      onClick={() => agregarAlCarrito(m)}
                      title={m.requiereReceta ? 'Requiere receta médica' : 'Agregar al carrito'}
                      className="w-8 h-8 rounded-full bg-amber-500 text-white flex items-center justify-center hover:bg-amber-600 disabled:bg-slate-200 disabled:text-slate-400 transition"
                    >
                      <Plus size={16} />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* ─── Cliente + Carrito + Pago ─────────────────────────── */}
        <div className="flex flex-col gap-6">

          {/* Cliente */}
          <ClienteSelector
            cliente={cliente}
            dniInput={dniInput}
            setDniInput={setDniInput}
            onBuscarDni={onBuscarDni}
            onAbrirNuevoCliente={() => setMostrarNuevoCliente(true)}
            limpiarCliente={limpiarCliente}
          />

          {/* Carrito */}
          <div className="bg-white border border-slate-200 rounded-2xl p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-slate-800 flex items-center gap-2"><ShoppingCart size={18} /> Carrito</h2>
              <span className="text-xs text-slate-400">{cart.reduce((a, i) => a + i.cantidad, 0)} ítem(s)</span>
            </div>

            {cart.length === 0 ? (
              <div className="text-center text-sm text-slate-400 py-10">
                Aún no agregaste productos.<br />Selecciónalos del catálogo.
              </div>
            ) : (
              <div className="flex flex-col divide-y divide-slate-100">
                {cart.map((i) => (
                  <div key={i.id} className="flex items-center justify-between py-3 gap-3">
                    <div className="min-w-0">
                      <div className="text-sm font-semibold text-slate-800 truncate">{i.nombre}</div>
                      <div className="text-xs text-slate-400 font-mono">S/ {i.precio.toFixed(2)} c/u</div>
                    </div>
                    <div className="flex items-center gap-2 shrink-0">
                      <button onClick={() => cambiarCantidad(i.id, -1)} className="w-6 h-6 rounded-md border border-slate-200 flex items-center justify-center hover:bg-slate-50">
                        <Minus size={12} />
                      </button>
                      <span className="text-sm w-5 text-center">{i.cantidad}</span>
                      <button onClick={() => cambiarCantidad(i.id, 1)} disabled={i.cantidad >= i.stock} className="w-6 h-6 rounded-md border border-slate-200 flex items-center justify-center hover:bg-slate-50 disabled:opacity-40">
                        <Plus size={12} />
                      </button>
                    </div>
                    <div className="w-20 text-right text-sm font-semibold text-slate-800 shrink-0">
                      S/ {(i.precio * i.cantidad).toFixed(2)}
                    </div>
                    <button onClick={() => quitarDelCarrito(i.id)} className="text-slate-300 hover:text-red-500 shrink-0">
                      <X size={14} />
                    </button>
                  </div>
                ))}
              </div>
            )}

            <div className="mt-4 pt-4 border-t border-slate-100 flex flex-col gap-1.5 text-sm">
              <div className="flex justify-between text-slate-500"><span>Subtotal</span><span>S/ {totales.subtotal.toFixed(2)}</span></div>
              <div className="flex justify-between text-slate-500"><span>IGV (18%)</span><span>S/ {totales.igv.toFixed(2)}</span></div>
              <div className="flex justify-between text-base font-bold text-slate-900 pt-1"><span>Total</span><span>S/ {totales.total.toFixed(2)}</span></div>
            </div>
          </div>

          {/* Pago */}
          <div className="bg-white border border-slate-200 rounded-2xl p-6">
            <h2 className="text-lg font-bold text-slate-800 mb-4">Cobro</h2>

            <div className="grid grid-cols-3 gap-2 mb-4">
              {METODOS_PAGO.map(({ value, label, icon: Icon }) => (
                <button
                  key={value}
                  onClick={() => setMetodoPago(value)}
                  className={`flex flex-col items-center gap-1.5 border rounded-xl py-3 text-xs font-semibold transition ${
                    metodoPago === value ? 'border-blue-500 bg-blue-50 text-blue-700' : 'border-slate-200 text-slate-500 hover:border-slate-300'
                  }`}
                >
                  <Icon size={16} />
                  {label}
                </button>
              ))}
            </div>

            <Select
              label="Comprobante"
              name="tipoComprobante"
              value={tipoComprobante}
              onChange={(e) => setTipoComprobante(e.target.value)}
              options={COMPROBANTES}
            />

            <Button
              className="w-full mt-4 py-3 text-sm"
              disabled={!puedeCobrar}
              loading={procesando}
              onClick={procesarPago}
            >
              Procesar pago · S/ {totales.total.toFixed(2)}
            </Button>
            {!cliente && cart.length > 0 && (
              <p className="text-xs text-amber-600 mt-2 text-center">Selecciona un cliente para poder cobrar.</p>
            )}
          </div>
        </div>
      </div>

      {/* Modal: nuevo cliente rápido */}
      <NuevoClienteModal
        isOpen={mostrarNuevoCliente}
        onClose={() => setMostrarNuevoCliente(false)}
        nuevoCliente={nuevoCliente}
        setNuevoCliente={setNuevoCliente}
        onConfirm={onCrearClienteRapido}
      />

      {/* Modal: ticket de venta exitosa */}
      {ultimaVenta && (
        <Modal
          isOpen={!!ultimaVenta}
          title="Venta procesada"
          onClose={cerrarTicket}
          onConfirm={cerrarTicket}
          confirmText="Cerrar"
        >
          <div className="flex flex-col items-center text-center gap-2 mb-4">
            <CheckCircle2 className="text-emerald-500" size={40} />
            <div className="text-sm text-slate-500">Venta #{ultimaVenta.id} · {ultimaVenta.nombreCliente}</div>
          </div>
          <div className="flex flex-col divide-y divide-slate-100 text-sm mb-3">
            {ultimaVenta.detalles.map((d) => (
              <div key={d.medicamentoId} className="flex justify-between py-1.5">
                <span>{d.cantidad}× {d.nombreMedicamento}</span>
                <span>S/ {d.subtotal.toFixed(2)}</span>
              </div>
            ))}
          </div>
          <div className="flex justify-between text-base font-bold text-slate-900 border-t border-slate-100 pt-3">
            <span>Total</span><span>S/ {ultimaVenta.total.toFixed(2)}</span>
          </div>
        </Modal>
      )}
      </>
      )}
    </div>
  );
}
