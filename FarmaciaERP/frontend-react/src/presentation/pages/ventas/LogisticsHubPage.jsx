import { Truck, Package, CheckCircle, Clock, AlertCircle } from 'lucide-react';

const ORDENES = [
  { id: 'OC-2024-1523', estado: 'En Proceso', estadoColor: 'bg-violet-600 text-white', cliente: 'Distribuidora Farmacéutica Los Andes S.A.C.', items: 15, unidades: 458, pickeado: '12/15', cajas: 8, peso: '142 kg', operador: 'Juan Pérez', accion: 'Confirmar Despacho', accionColor: 'bg-green-500 hover:bg-green-600' },
  { id: 'OC-2024-1524', estado: 'Pendiente',  estadoColor: 'bg-gray-200 text-gray-700', cliente: 'Droguería Comercial Peruana EIRL', items: 8, unidades: 124, pickeado: null, cajas: null, peso: null, operador: null, accion: 'Iniciar Picking', accionColor: 'bg-blue-600 hover:bg-blue-700' },
];

const ENTREGAS = [
  { label: 'Entregado en Mostrador', value: 47, icon: CheckCircle, color: 'text-green-600', bg: 'bg-green-50' },
  { label: 'En Preparación',         value: 12, icon: Package,     color: 'text-blue-600',  bg: 'bg-blue-50'  },
  { label: 'Delivery Express',       value: 8,  icon: Truck,       color: 'text-violet-600',bg: 'bg-violet-50'},
  { label: 'Pendientes',             value: 5,  icon: Clock,       color: 'text-amber-600', bg: 'bg-amber-50' },
];

export default function LogisticsHubPage() {
  return (
    <div>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-gray-900">Logistics Fulfillment Hub - Monitor de Entregas y Despacho</h2>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">

        {/* ATP */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 font-semibold text-gray-700 mb-4">
            <AlertCircle size={16} className="text-blue-600" />
            ATP - Available to Promise
          </div>
          {[
            { label: 'Stock Local (Tienda 01)', value: '124 und', pct: 62, color: 'bg-blue-500'  },
            { label: 'Almacén Central',          value: '1,458 und', pct: 85, color: 'bg-green-500' },
            { label: 'En Tránsito',              value: '245 und', pct: 30, color: 'bg-amber-500', sub: 'ETA: 2 días' },
          ].map((s) => (
            <div key={s.label} className="mb-4">
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600">{s.label}</span>
                <span className="font-bold text-blue-600">{s.value}</span>
              </div>
              <div className="w-full bg-gray-100 rounded-full h-2">
                <div className={`${s.color} h-2 rounded-full`} style={{ width: `${s.pct}%` }} />
              </div>
              {s.sub && <p className="text-xs text-gray-400 mt-1">{s.sub}</p>}
            </div>
          ))}
          <div className="bg-gray-50 rounded-xl p-4 mt-2">
            <p className="text-xs text-gray-500">Total Disponible para Promesa</p>
            <p className="text-2xl font-bold text-gray-800">1,827 und</p>
          </div>
        </div>

        {/* Picking */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 font-semibold text-gray-700">
              <Package size={16} className="text-violet-600" />
              Consola de Picking y Packing (B2B)
            </div>
            <button className="bg-violet-600 text-white text-xs px-3 py-1.5 rounded-lg hover:bg-violet-700 transition">
              Generar Ruta de Picking
            </button>
          </div>
          <div className="flex flex-col gap-3">
            {ORDENES.map((o) => (
              <div key={o.id} className="border-l-4 border-violet-400 bg-gray-50 rounded-xl p-4">
                <div className="flex items-center gap-2 mb-2">
                  <span className={`text-xs font-bold px-2 py-0.5 rounded ${o.estadoColor}`}>{o.id}</span>
                  <span className={`text-xs px-2 py-0.5 rounded-full ${o.estadoColor}`}>{o.estado}</span>
                  {o.operador && <span className="text-xs text-gray-500 ml-auto">Operador: <span className="font-medium">{o.operador}</span></span>}
                </div>
                <p className="text-sm font-semibold text-gray-700 mb-1">{o.cliente}</p>
                <p className="text-xs text-gray-400 mb-3">{o.items} ítems • {o.unidades} unidades totales</p>
                {o.pickeado && (
                  <div className="grid grid-cols-3 gap-2 mb-3 text-center">
                    {[{ label: 'Pickeado', value: o.pickeado }, { label: 'Cajas Armadas', value: o.cajas }, { label: 'Peso Total', value: o.peso }].map((d) => (
                      <div key={d.label}>
                        <p className="text-xs text-gray-500">{d.label}</p>
                        <p className="text-sm font-bold text-green-600">{d.value}</p>
                      </div>
                    ))}
                  </div>
                )}
                <div className="flex gap-2">
                  <button className={`flex-1 ${o.accionColor} text-white text-xs py-2 rounded-lg font-medium transition`}>{o.accion}</button>
                  {o.pickeado && <button className="border border-gray-200 text-gray-600 text-xs px-3 py-2 rounded-lg hover:bg-gray-50 transition">Imprimir Guía</button>}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Despacho B2C */}
      <div className="bg-white rounded-2xl border border-gray-200 p-5">
        <div className="flex items-center gap-2 font-semibold text-gray-700 mb-4">
          <Truck size={16} className="text-teal-600" />
          Despacho Inmediato (B2C) - Entregas del Día
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          {ENTREGAS.map((e) => (
            <div key={e.label} className={`${e.bg} rounded-xl p-4`}>
              <e.icon size={20} className={`${e.color} mb-2`} />
              <p className={`text-3xl font-bold ${e.color}`}>{e.value}</p>
              <p className="text-xs text-gray-500 mt-1">{e.label}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}