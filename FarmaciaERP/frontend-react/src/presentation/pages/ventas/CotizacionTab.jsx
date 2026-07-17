import { useEffect, useState } from 'react';
import { Search, Plus, Minus, X, FileText, CheckCircle2, XCircle, Clock, ShoppingCart } from 'lucide-react';
import Button from '@/presentation/components/ui/Button';
import Select from '@/presentation/components/ui/Select';
import ClienteSelector from '@/presentation/components/ventas/ClienteSelector';
import NuevoClienteModal from '@/presentation/components/ventas/NuevoClienteModal';
import { useCotizacion } from '@/presentation/hooks/useCotizacion';
import { useClienteSelector } from '@/presentation/hooks/useClienteSelector';
import { VIGENCIAS_COTIZACION, MOTIVOS_RECHAZO_COTIZACION } from '@/domain/models/Cotizacion';

const ESTADO_BADGE = {
  PENDIENTE: { label: 'Pendiente', className: 'bg-amber-50 text-amber-700 border border-amber-200', icon: Clock },
  ACEPTADA:  { label: 'Aceptada',  className: 'bg-emerald-50 text-emerald-700 border border-emerald-200', icon: CheckCircle2 },
  RECHAZADA: { label: 'Rechazada', className: 'bg-red-50 text-red-600 border border-red-200', icon: XCircle },
  VENCIDA:   { label: 'Vencida',   className: 'bg-slate-100 text-slate-500 border border-slate-200', icon: Clock },
};

function CantidadInput({ id, cantidad, establecerCantidad }) {
  const [val, setVal] = useState(cantidad.toString());

  useEffect(() => {
    setVal(cantidad.toString());
  }, [cantidad]);

  const handleChange = (e) => {
    const rawValue = e.target.value;
    if (rawValue === '' || /^\d+$/.test(rawValue)) {
      setVal(rawValue);
      const parsed = parseInt(rawValue, 10);
      if (!isNaN(parsed) && parsed > 0) {
        establecerCantidad(id, parsed);
      }
    }
  };

  const handleBlur = () => {
    const parsed = parseInt(val, 10);
    if (isNaN(parsed) || parsed <= 0) {
      setVal('1');
      establecerCantidad(id, 1);
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

export default function CotizacionTab({ medicamentos, loadingCatalogo, cargarCatalogo }) {
  const {
    items, agregarItem, cambiarCantidad, establecerCantidad, quitarItem, totalCotizado,
    vigenciaDias, setVigenciaDias,
    historial, loadingHistorial, cargarHistorial,
    enviando, enviarCotizacion,
    procesandoId, aceptarCotizacion, rechazarCotizacion,
    error, setError,
  } = useCotizacion();

  const {
    cliente, buscarClientePorDni, registrarClienteRapido, limpiarCliente, errorCliente,
  } = useClienteSelector();

  const [busqueda, setBusqueda] = useState('');
  const [dniInput, setDniInput] = useState('');
  const [mostrarNuevoCliente, setMostrarNuevoCliente] = useState(false);
  const [nuevoCliente, setNuevoCliente] = useState({ nombre: '', apellido: '', dni: '', tipoSeguro: 'SIN_SEGURO' });
  const [motivos, setMotivos] = useState({}); // { [cotizacionId]: motivoSeleccionado }
  const [ultimaVentaGenerada, setUltimaVentaGenerada] = useState(null);

  useEffect(() => {
    cargarHistorial();
  }, [cargarHistorial]);

  const errorVisible = error ?? errorCliente;

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

  async function onEnviar() {
    if (!cliente) { setError('Selecciona un cliente para enviar la cotización'); return; }
    await enviarCotizacion(cliente.id);
  }

  async function onAceptar(id) {
    const venta = await aceptarCotizacion(id);
    if (venta) setUltimaVentaGenerada(venta);
  }

  async function onRechazar(id) {
    const motivo = motivos[id] ?? MOTIVOS_RECHAZO_COTIZACION[0];
    await rechazarCotizacion(id, motivo);
  }

  const puedeEnviar = items.length > 0 && !!cliente && !enviando;

  return (
    <div className="max-w-[1400px] mx-auto">
      {errorVisible && (
        <div className="mb-4 flex items-start justify-between gap-4 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl px-4 py-3">
          <span>{errorVisible}</span>
          <button onClick={() => setError(null)} className="text-red-400 hover:text-red-600">
            <X size={16} />
          </button>
        </div>
      )}

      {ultimaVentaGenerada && (
        <div className="mb-4 flex items-start justify-between gap-4 bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm rounded-xl px-4 py-3">
          <span className="flex items-center gap-2">
            <CheckCircle2 size={16} />
            Cotización aceptada: se generó el pedido de venta #{ultimaVentaGenerada.id} ({ultimaVentaGenerada.nombreCliente}) por S/ {ultimaVentaGenerada.total.toFixed(2)}.
          </span>
          <button onClick={() => setUltimaVentaGenerada(null)} className="text-emerald-500 hover:text-emerald-700 shrink-0">
            <X size={16} />
          </button>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-[1.4fr_1fr] gap-6 items-start">
        {/* ─── Catálogo ─────────────────────────────────────────── */}
        <div className="bg-white border border-slate-200 rounded-2xl p-6">
          <h2 className="text-lg font-bold text-slate-800 mb-1">Construir oferta económica</h2>
          <p className="text-xs text-slate-500 mb-4">SD.02.01–02.02 · Busca productos y agrégalos a la cotización.</p>

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
            {!loadingCatalogo && medicamentos.map((m) => (
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
                    onClick={() => agregarItem(m)}
                    title="Agregar a la cotización"
                    className="w-8 h-8 rounded-full bg-amber-500 text-white flex items-center justify-center hover:bg-amber-600 transition"
                  >
                    <Plus size={16} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* ─── Cliente + Cotización en curso + Historial ────────── */}
        <div className="flex flex-col gap-6">

          <ClienteSelector
            cliente={cliente}
            dniInput={dniInput}
            setDniInput={setDniInput}
            onBuscarDni={onBuscarDni}
            onAbrirNuevoCliente={() => setMostrarNuevoCliente(true)}
            limpiarCliente={limpiarCliente}
          />

          {/* Cotización en curso */}
          <div className="bg-white border border-slate-200 rounded-2xl p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-slate-800 flex items-center gap-2"><FileText size={18} /> Cotización en curso</h2>
              <span className="text-xs text-slate-400">{items.reduce((a, i) => a + i.cantidad, 0)} ítem(s)</span>
            </div>

            {items.length === 0 ? (
              <div className="text-center text-sm text-slate-400 py-10">
                Agrega productos para armar la cotización.
              </div>
            ) : (
              <div className="flex flex-col divide-y divide-slate-100">
                {items.map((i) => (
                  <div key={i.id} className="flex items-center justify-between py-3 gap-3">
                    <div className="min-w-0">
                      <div className="text-sm font-semibold text-slate-800 truncate">{i.nombre}</div>
                      <div className="text-xs text-slate-400 font-mono">S/ {i.precio.toFixed(2)} c/u</div>
                    </div>
                    <div className="flex items-center gap-2 shrink-0">
                      <button onClick={() => cambiarCantidad(i.id, -1)} className="w-6 h-6 rounded-md border border-slate-200 flex items-center justify-center hover:bg-slate-50">
                        <Minus size={12} />
                      </button>
                      <CantidadInput id={i.id} cantidad={i.cantidad} establecerCantidad={establecerCantidad} />
                      <button onClick={() => cambiarCantidad(i.id, 1)} className="w-6 h-6 rounded-md border border-slate-200 flex items-center justify-center hover:bg-slate-50">
                        <Plus size={12} />
                      </button>
                    </div>
                    <div className="w-20 text-right text-sm font-semibold text-slate-800 shrink-0">
                      S/ {(i.precio * i.cantidad).toFixed(2)}
                    </div>
                    <button onClick={() => quitarItem(i.id)} className="text-slate-300 hover:text-red-500 shrink-0">
                      <X size={14} />
                    </button>
                  </div>
                ))}
              </div>
            )}

            <div className="mt-4 pt-4 border-t border-slate-100 flex justify-between text-base font-bold text-slate-900">
              <span>Total cotizado</span><span>S/ {totalCotizado.toFixed(2)}</span>
            </div>

            <div className="mt-4">
              <Select
                label="Vigencia de la oferta"
                name="vigenciaDias"
                value={vigenciaDias}
                onChange={(e) => setVigenciaDias(Number(e.target.value))}
                options={VIGENCIAS_COTIZACION.map((v) => ({ value: v.value, label: v.label }))}
              />
            </div>

            <Button
              className="w-full mt-4 py-3 text-sm"
              disabled={!puedeEnviar}
              loading={enviando}
              onClick={onEnviar}
            >
              Enviar cotización al cliente
            </Button>
            {!cliente && items.length > 0 && (
              <p className="text-xs text-amber-600 mt-2 text-center">Selecciona un cliente para poder enviar la cotización.</p>
            )}
          </div>

          {/* Historial / estatus */}
          <div className="bg-white border border-slate-200 rounded-2xl p-6">
            <h2 className="text-lg font-bold text-slate-800 mb-1">Estatus de cotizaciones</h2>
            <p className="text-xs text-slate-500 mb-4">SD.02.03 · Monitorea el estado de cada oferta enviada.</p>

            {loadingHistorial && (
              <div className="text-center text-sm text-slate-400 py-6">Cargando historial...</div>
            )}
            {!loadingHistorial && historial.length === 0 && (
              <div className="text-center text-sm text-slate-400 py-6">Aún no hay cotizaciones registradas.</div>
            )}

            <div className="flex flex-col gap-3 max-h-[420px] overflow-y-auto pr-1 scrollbar-hide">
              {historial.map((c) => {
                const badge = ESTADO_BADGE[c.estado] ?? ESTADO_BADGE.PENDIENTE;
                const Icon = badge.icon;
                const itemsTxt = c.detalles.map((d) => `${d.cantidad}× ${d.nombreMedicamento}`).join(', ');
                return (
                  <div key={c.id} className="border border-slate-200 rounded-xl px-4 py-3">
                    <div className="flex items-center justify-between gap-2 mb-1">
                      <span className="text-sm font-semibold text-slate-800">COT-{c.id}</span>
                      <span className={`inline-flex items-center gap-1 text-[11px] font-medium px-2 py-0.5 rounded-full ${badge.className}`}>
                        <Icon size={11} /> {badge.label}
                      </span>
                      <span className="text-sm font-semibold text-slate-800 ml-auto">S/ {c.total.toFixed(2)}</span>
                    </div>
                    <div className="text-xs text-slate-500">{c.nombreCliente} · {itemsTxt}</div>
                    <div className="text-xs text-slate-400 mt-0.5">
                      Vigencia {c.vigenciaDias} días {!c.vigente && c.estado === 'PENDIENTE' && '· Vencida'}
                    </div>

                    {c.estado === 'PENDIENTE' && (
                      <div className="flex items-center gap-2 mt-3">
                        <button
                          disabled={procesandoId === c.id || !c.vigente}
                          onClick={() => onAceptar(c.id)}
                          className="flex items-center gap-1 text-xs font-semibold px-3 py-1.5 rounded-lg bg-emerald-50 text-emerald-700 hover:bg-emerald-100 disabled:opacity-40 transition"
                        >
                          <ShoppingCart size={12} /> Cliente acepta
                        </button>
                        <select
                          value={motivos[c.id] ?? MOTIVOS_RECHAZO_COTIZACION[0]}
                          onChange={(e) => setMotivos((p) => ({ ...p, [c.id]: e.target.value }))}
                          className="text-xs border border-slate-200 rounded-lg px-2 py-1.5 outline-none"
                        >
                          {MOTIVOS_RECHAZO_COTIZACION.map((m) => <option key={m} value={m}>{m}</option>)}
                        </select>
                        <button
                          disabled={procesandoId === c.id}
                          onClick={() => onRechazar(c.id)}
                          className="flex items-center gap-1 text-xs font-semibold px-3 py-1.5 rounded-lg bg-red-50 text-red-600 hover:bg-red-100 disabled:opacity-40 transition"
                        >
                          Cliente rechaza
                        </button>
                      </div>
                    )}
                    {c.estado === 'RECHAZADA' && (
                      <div className="text-xs text-red-500 mt-2">Motivo de rechazo: {c.motivoRechazo}</div>
                    )}
                    {c.estado === 'ACEPTADA' && (
                      <div className="text-xs text-emerald-600 mt-2">Convertida a pedido de venta #{c.ventaGeneradaId}</div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </div>

      <NuevoClienteModal
        isOpen={mostrarNuevoCliente}
        onClose={() => setMostrarNuevoCliente(false)}
        nuevoCliente={nuevoCliente}
        setNuevoCliente={setNuevoCliente}
        onConfirm={onCrearClienteRapido}
      />
    </div>
  );
}
