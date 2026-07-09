import { useState }  from 'react';
import { useVentas } from '@/presentation/hooks/useVentas';
import { ShoppingCart, Plus, Minus, X, Wifi, User, CreditCard, FileText } from 'lucide-react';
import { useClientes } from '@/presentation/hooks/useClientes';

const PRODUCTOS_DEMO = [
  { id: 2, nombre: 'Paracetamol 500mg Caja x 20 tabletas', sku: '7501000000002', precio: 4.50 },
  { id: 3, nombre: 'Ibuprofeno 400mg Caja x 10 cápsulas', sku: '7501000000003', precio: 6.20 },
  { id: 4, nombre: 'Amoxicilina 500mg Frasco Suspensión 60ml', sku: '7501000000004', precio: 12.80 },
  { id: 5, nombre: 'Loratadina 10mg Caja x 30 tabletas', sku: '7501000000005', precio: 5.00 },
  { id: 6, nombre: 'Aspirina 100mg Caja x 100 tabletas', sku: '7501000000006', precio: 3.50 },
  { id: 7, nombre: 'Omeprazol 20mg Caja x 14 cápsulas', sku: '7501000000007', precio: 8.90 },
  { id: 8, nombre: 'Panadol Antigripal Caja x 24 tabletas', sku: '7501000000008', precio: 7.50 },
  { id: 9, nombre: 'Salbutamol 100mcg Inhalador 200 dosis', sku: '7501000000009', precio: 18.20 },
  { id: 10, nombre: 'Metformina 850mg Caja x 30 tabletas', sku: '7501000000010', precio: 14.00 },
  { id: 11, nombre: 'Losartán 500mg Caja x 30 tabletas', sku: '7501000000011', precio: 11.50 },
  { id: 12, nombre: 'Prednisona 5mg Caja x 20 tabletas', sku: '7501000000012', precio: 5.80 },
  { id: 13, nombre: 'Fluconazol 150mg Caja x 2 cápsulas', sku: '7501000000013', precio: 9.00 },
  { id: 14, nombre: 'Vitamina C 1g Tubo x 10 tabletas efervescentes', sku: '7501000000014', precio: 15.00 },
  { id: 15, nombre: 'Clonazepam 2mg Caja x 30 tabletas', sku: '7501000000015', precio: 22.00 },
  { id: 16, nombre: 'Morfina 10mg/ml Ampolla 1ml', sku: '7501000000016', precio: 55.50 }
];
const METODOS_PAGO = [
  { value: 'EFECTIVO',      label: 'Efectivo'       },
  { value: 'TARJETA',       label: 'Tarjeta'        },
  { value: 'YAPE',          label: 'Yape'           },
  { value: 'PLIN',          label: 'Plin'           },
  { value: 'TRANSFERENCIA', label: 'Transferencia'  },
];

const TIPOS_COMPROBANTE = [
  { value: 'BOLETA',   label: 'Boleta de venta'    },
  { value: 'FACTURA',  label: 'Factura'            },
  { value: 'NINGUNO',   label: 'Sin Comprobante'   },
];

const IGV = 0.18;

export default function CheckoutPage() {
  const { crearVenta, pagarVenta, loading, error } = useVentas();

  const [busqueda,        setBusqueda]        = useState('');
  const [carrito,         setCarrito]         = useState([]);
  const [tipoCliente,     setTipoCliente]     = useState('consumidor');
  const [metodoPago,      setMetodoPago]      = useState('EFECTIVO');
  const [tipoComprobante, setTipoComprobante] = useState('BOLETA');
  const [success,         setSuccess]         = useState(false);
  const [ventaId,         setVentaId]         = useState(null);
  const [clienteId, setClienteId] = useState(null);
  const productosFiltrados = PRODUCTOS_DEMO.filter((p) =>
    p.nombre.toLowerCase().includes(busqueda.toLowerCase()) ||
    p.sku.includes(busqueda)
  );
  const { clientes, loading: loadingClientes } = useClientes();
const [busquedaCliente, setBusquedaCliente] = useState('');
const [clienteSeleccionado, setClienteSeleccionado] = useState(null);
const [showDropdown, setShowDropdown] = useState(false);

const clientesFiltrados = clientes.filter((c) => {
  const q      = busquedaCliente.toLowerCase();
  const nombre = c.nombres?.value?.toLowerCase() ?? '';
  const dni    = c.dni?.dni ?? '';

  // ← filtra por tipo de seguro según el tipo de cliente
  if (tipoCliente === 'corporativo' && c.tipoSeguro !== 'ESSALUD') return false;
  if (tipoCliente === 'consumidor' && c.tipoSeguro == 'ESSALUD')return false;
  return nombre.includes(q) || dni.includes(q);
});
  function agregarProducto(producto) {
    setCarrito((prev) => {
      const existe = prev.find((i) => i.id === producto.id);
      if (existe) return prev.map((i) => i.id === producto.id ? { ...i, cantidad: i.cantidad + 1 } : i);
      return [...prev, { ...producto, cantidad: 1 }];
    });
    setBusqueda('');
  }
  function seleccionarCliente(cliente) {
  setClienteSeleccionado(cliente);
  setClienteId(cliente.id);
  setBusquedaCliente(cliente.nombres?.value ?? '');
  setShowDropdown(false);
}

function limpiarCliente() {
  setClienteSeleccionado(null);
  setClienteId(null);
  setBusquedaCliente('');
}
  function cambiarCantidad(id, delta) {
    setCarrito((prev) =>
      prev.map((i) => i.id === id ? { ...i, cantidad: Math.max(1, i.cantidad + delta) } : i)
    );
  }

  function eliminarItem(id) {
    setCarrito((prev) => prev.filter((i) => i.id !== id));
  }

  const subtotal = carrito.reduce((acc, i) => acc + i.precio * i.cantidad, 0);
  const igv      = subtotal * IGV;
  const total    = subtotal + igv;

  async function handleProcesarPago() {
    if (carrito.length === 0) return;

    const formData = {
      clienteId:       clienteId ?? null,
      metodoPago:      metodoPago,
      tipoComprobante: tipoComprobante,
      detalles:        carrito.map((i) => ({
        productoId: i.id,
        cantidad:   i.cantidad,
      })),
    };

    const nueva = await crearVenta(formData);
    if (nueva) {
      const ok = await pagarVenta(nueva.id);
      if (ok) {
        setVentaId(nueva.id);
        setSuccess(true);
        setCarrito([]);
        setTimeout(() => setSuccess(false), 4000);
      }
    }
  }

  return (
    <div>
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold text-gray-900">Checkout Frontend - Punto de Venta (POS)</h2>
        <div className="flex items-center gap-2 text-green-600 bg-green-50 border border-green-200 px-3 py-1.5 rounded-lg text-sm font-medium">
          <Wifi size={14} /> Online
        </div>
      </div>

      {/* Success */}
      {success && (
        <div className="mb-4 px-4 py-3 bg-green-50 border border-green-200 rounded-xl text-sm text-green-700 font-medium flex items-center gap-2">
          ✅ Venta #{ventaId} procesada exitosamente
        </div>
      )}

      {/* Error */}
      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
          ⚠ {error}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

        {/* Panel izquierdo */}
        <div className="lg:col-span-2 flex flex-col gap-4">

          {/* Buscador */}
          <div className="bg-white rounded-2xl border border-gray-200 p-5">
            <p className="text-sm font-semibold text-gray-700 mb-3 flex items-center gap-2">
              <ShoppingCart size={16} className="text-gray-400" />
              Escáner de Ítems
            </p>
            <input
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              placeholder="Escanear código de barras o buscar producto..."
              className="w-full border border-gray-200 rounded-xl px-4 py-3 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
            />
            {busqueda && (
              <div className="mt-2 border border-gray-100 rounded-xl overflow-hidden shadow-sm">
                {productosFiltrados.length === 0 ? (
                  <p className="px-4 py-3 text-sm text-gray-400">Sin resultados</p>
                ) : (
                  productosFiltrados.map((p) => (
                    <button
                      key={p.id}
                      onClick={() => agregarProducto(p)}
                      className="w-full px-4 py-3 text-left hover:bg-blue-50 transition flex items-center justify-between border-b border-gray-50 last:border-0"
                    >
                      <div>
                        <p className="text-sm font-medium text-gray-700">{p.nombre}</p>
                        <p className="text-xs text-gray-400">{p.sku}</p>
                      </div>
                      <p className="text-sm font-semibold text-blue-600">S/ {p.precio.toFixed(2)}</p>
                    </button>
                  ))
                )}
              </div>
            )}
          </div>

          {/* Carrito */}
          <div className="bg-white rounded-2xl border border-gray-200 overflow-hidden">
            <div className="px-5 py-4 bg-blue-600 flex items-center justify-between">
              <div className="flex items-center gap-2 text-white font-semibold">
                <ShoppingCart size={16} /> Carrito de Compra
              </div>
              <span className="bg-white/20 text-white text-xs px-2 py-0.5 rounded-full">
                {carrito.length} items
              </span>
            </div>

            {carrito.length === 0 ? (
              <div className="py-12 text-center text-gray-400 text-sm">
                Agrega productos al carrito
              </div>
            ) : (
              <div className="divide-y divide-gray-100">
                {carrito.map((item) => (
                  <div key={item.id} className="px-5 py-4">
                    <div className="flex items-start justify-between mb-2">
                      <div>
                        <p className="text-sm font-medium text-gray-700">{item.nombre}</p>
                        <p className="text-xs text-gray-400">{item.sku}</p>
                      </div>
                      <button onClick={() => eliminarItem(item.id)} className="text-red-400 hover:text-red-600 transition ml-2">
                        <X size={16} />
                      </button>
                    </div>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <button onClick={() => cambiarCantidad(item.id, -1)} className="w-7 h-7 border border-gray-200 rounded-lg flex items-center justify-center hover:bg-gray-50 transition">
                          <Minus size={12} />
                        </button>
                        <span className="text-sm font-semibold w-6 text-center">{item.cantidad}</span>
                        <button onClick={() => cambiarCantidad(item.id, 1)} className="w-7 h-7 border border-gray-200 rounded-lg flex items-center justify-center hover:bg-gray-50 transition">
                          <Plus size={12} />
                        </button>
                      </div>
                      <p className="text-sm font-bold text-gray-800">
                        S/ {(item.precio * item.cantidad).toFixed(2)}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Panel derecho */}
        <div className="flex flex-col gap-4">

          {/* Cliente */}
          {/* Cliente */}
<div className="bg-white rounded-2xl border border-gray-200 p-5">
  <p className="text-sm font-semibold text-gray-700 mb-3 flex items-center gap-2">
    <User size={16} className="text-gray-400" />
    Identificador de Cliente
  </p>

  {/* Tipo cliente */}
  <div className="flex gap-2 mb-3">
    <button
      onClick={() => { setTipoCliente('consumidor'); setTipoComprobante('BOLETA'); limpiarCliente(); }}
      className={`flex-1 py-2 rounded-lg text-sm font-medium transition ${tipoCliente === 'consumidor' ? 'bg-violet-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
    >
      Consumidor Final
    </button>
    <button
      onClick={() => { setTipoCliente('corporativo'); setTipoComprobante('FACTURA'); limpiarCliente(); }}
      className={`flex-1 py-2 rounded-lg text-sm font-medium transition ${tipoCliente === 'corporativo' ? 'bg-violet-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
    >
      Corporativo
    </button>
  </div>

  {/* Buscador de cliente */}
  <div className="relative">
    <input
      value={busquedaCliente}
      onChange={(e) => {
        setBusquedaCliente(e.target.value);
        setShowDropdown(true);
        if (!e.target.value) limpiarCliente();
      }}
      onFocus={() => setShowDropdown(true)}
      placeholder={tipoCliente === 'consumidor' ? 'Buscar por DNI o nombre...' : 'Buscar por RUC o razón social...'}
      className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
    />

    {/* Dropdown resultados */}
    {showDropdown && busquedaCliente && !clienteSeleccionado && (
      <div className="absolute z-10 w-full mt-1 bg-white border border-gray-200 rounded-xl shadow-lg overflow-hidden max-h-48 overflow-y-auto">
        {loadingClientes ? (
          <p className="px-4 py-3 text-sm text-gray-400">Cargando...</p>
        ) : clientesFiltrados.length === 0 ? (
          <p className="px-4 py-3 text-sm text-gray-400">Sin resultados</p>
        ) : (
          clientesFiltrados.map((c) => (
            <button
              key={c.id}
              onClick={() => seleccionarCliente(c)}
              className="w-full px-4 py-3 text-left hover:bg-blue-50 transition border-b border-gray-50 last:border-0"
            >
              <p className="text-sm font-medium text-gray-700">{c.nombres?.value}</p>
              <div className="flex gap-3 text-xs text-gray-400 mt-0.5">
                <span>DNI: {c.dni?.dni}</span>
                <span>{c.tipoSeguro}</span>
              </div>
            </button>
          ))
        )}
      </div>
    )}
  </div>

  {/* Cliente seleccionado */}
  {clienteSeleccionado && (
    <div className="mt-3 bg-blue-50 border border-blue-200 rounded-xl px-4 py-3 flex items-center justify-between">
      <div>
        <p className="text-sm font-semibold text-blue-700">{clienteSeleccionado.nombres?.value}</p>
        <div className="flex gap-3 text-xs text-blue-500 mt-0.5">
          <span>DNI: {clienteSeleccionado.dni?.dni}</span>
          <span>{clienteSeleccionado.tipoSeguro}</span>
        </div>
      </div>
      <button onClick={limpiarCliente} className="text-blue-400 hover:text-blue-600 transition">
        <X size={16} />
      </button>
    </div>
  )}
</div>

          {/* Comprobante */}
          <div className="bg-white rounded-2xl border border-gray-200 p-5">
            <p className="text-sm font-semibold text-gray-700 mb-3 flex items-center gap-2">
              <FileText size={16} className="text-gray-400" />
              Tipo de Comprobante
            </p>
            <div className="flex gap-2">
              {TIPOS_COMPROBANTE.map((t) => (
                <button
                  key={t.value}
                  onClick={() => setTipoComprobante(t.value)}
                  className={`flex-1 py-2 rounded-lg text-xs font-medium transition ${tipoComprobante === t.value ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
                >
                  {t.label}
                </button>
              ))}
            </div>
          </div>

          {/* Método de pago */}
          <div className="bg-white rounded-2xl border border-gray-200 p-5">
            <p className="text-sm font-semibold text-gray-700 mb-3 flex items-center gap-2">
              <CreditCard size={16} className="text-gray-400" />
              Método de Pago
            </p>
            <div className="flex flex-col gap-2">
              {METODOS_PAGO.map((m) => (
                <button
                  key={m.value}
                  onClick={() => setMetodoPago(m.value)}
                  className={`flex items-center gap-2 px-3 py-2.5 rounded-xl text-sm transition border ${
                    metodoPago === m.value
                      ? 'border-blue-500 bg-blue-50 text-blue-700 font-medium'
                      : 'border-gray-200 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <div className={`w-3 h-3 rounded-full border-2 ${metodoPago === m.value ? 'border-blue-500 bg-blue-500' : 'border-gray-300'}`} />
                  {m.label}
                </button>
              ))}
            </div>
          </div>

          {/* Resumen */}
          <div className="bg-white rounded-2xl border border-gray-200 p-5">
            <p className="text-sm font-semibold text-gray-700 mb-4">Resumen de Compra</p>
            <div className="flex flex-col gap-2 mb-4">
              <div className="flex justify-between text-sm text-gray-600">
                <span>Subtotal</span>
                <span>S/ {subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sm text-gray-600">
                <span>IGV (18%)</span>
                <span>S/ {igv.toFixed(2)}</span>
              </div>
              <div className="border-t border-gray-100 pt-2 flex justify-between font-bold text-base">
                <span>TOTAL</span>
                <span className="text-blue-600">S/ {total.toFixed(2)}</span>
              </div>
            </div>
            <button
              onClick={handleProcesarPago}
              disabled={carrito.length === 0 || loading}
              className="w-full bg-green-500 hover:bg-green-600 disabled:opacity-40 disabled:cursor-not-allowed text-white py-3 rounded-xl font-semibold transition flex items-center justify-center gap-2"
            >
              {loading ? (
                <div className="animate-spin h-4 w-4 border-2 border-white border-t-transparent rounded-full" />
              ) : (
                <CreditCard size={16} />
              )}
              Procesar Pago
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}