import { useNavigate } from 'react-router-dom';
import { useVentas }   from '@/presentation/hooks/useVentas';
import {
  ShoppingCart, Users, Tag, DollarSign,
  Truck, RotateCcw, TrendingUp, Clock, CheckCircle, XCircle
} from 'lucide-react';

const SUBMODULOS = [
  { key: 'checkout',    label: 'Checkout Frontend',   sub: 'Punto de Venta',          to: '/osd/checkout',    icon: ShoppingCart, color: 'bg-blue-500'   },
  { key: 'partners',    label: 'Business Partners',   sub: 'Clientes y Catálogo',     to: '/osd/partners',    icon: Users,        color: 'bg-pink-500'   },
  { key: 'pricing',     label: 'Pricing Engine',      sub: 'Precios y Promociones',   to: '/osd/pricing',     icon: Tag,          color: 'bg-orange-500' },
  { key: 'financial',   label: 'Financial & Fiscal',  sub: 'Cierre y Facturación',    to: '/osd/financial',   icon: DollarSign,   color: 'bg-violet-500' },
  { key: 'logistics',   label: 'Logistics Hub',       sub: 'Despacho y Entregas',     to: '/osd/logistics',   icon: Truck,        color: 'bg-teal-500'   },
  { key: 'reverseops',  label: 'Reverse Ops',         sub: 'Devoluciones',            to: '/osd/reverseops',  icon: RotateCcw,    color: 'bg-red-500'    },
];

export default function VentasPage() {
  const navigate              = useNavigate();
  const { ventas, loading }   = useVentas();

  const pagadas   = ventas.filter((v) => v.estado === 'PAGADA').length;
  const pendientes = ventas.filter((v) => v.estado === 'PENDIENTE').length;
  const anuladas  = ventas.filter((v) => v.estado === 'ANULADA').length;
  const totalMes  = ventas.reduce((acc, v) => v.estado === 'PAGADA' ? acc + (v.total ?? 0) : acc, 0);

  return (
    <div>
      {/* Header */}
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-gray-900">Omnichannel Sales & Distribution</h2>
        <p className="text-gray-500 text-sm mt-1">Sistema integrado de ventas multicanal, gestión de clientes y operaciones comerciales</p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-8">
        {[
          { label: 'Ventas del Mes',    value: `S/ ${totalMes.toFixed(0)}`, icon: TrendingUp,   color: 'text-blue-600',   bg: 'bg-blue-50'   },
          { label: 'Pagadas',           value: pagadas,                      icon: CheckCircle,  color: 'text-green-600',  bg: 'bg-green-50'  },
          { label: 'Pendientes',        value: pendientes,                   icon: Clock,        color: 'text-amber-600',  bg: 'bg-amber-50'  },
          { label: 'Anuladas',          value: anuladas,                     icon: XCircle,      color: 'text-red-600',    bg: 'bg-red-50'    },
        ].map((card) => (
          <div key={card.label} className={`${card.bg} rounded-2xl border border-gray-100 p-5`}>
            <div className="flex items-center justify-between mb-3">
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider">{card.label}</p>
              <card.icon size={16} className={card.color} />
            </div>
            {loading
              ? <div className="h-8 w-16 bg-gray-200 animate-pulse rounded" />
              : <p className={`text-3xl font-bold ${card.color}`}>{card.value}</p>
            }
          </div>
        ))}
      </div>

      {/* Submódulos */}
      <div className="mb-4 flex items-center justify-between">
        <h3 className="text-base font-semibold text-gray-700">Páginas</h3>
        <span className="text-xs text-blue-600 font-medium cursor-pointer hover:underline">Ver todos</span>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 gap-4 mb-8">
        {SUBMODULOS.map((mod) => (
          <button
            key={mod.key}
            onClick={() => navigate(mod.to)}
            className={`${mod.color} rounded-2xl p-6 text-white text-left hover:opacity-90 transition-all hover:shadow-lg group`}
          >
            <mod.icon size={28} className="mb-4 opacity-90" />
            <p className="font-bold text-base">{mod.label}</p>
            <p className="text-white/70 text-sm mt-1">{mod.sub}</p>
          </button>
        ))}
      </div>

      {/* Ventas recientes */}
      <div className="bg-white rounded-2xl border border-gray-200 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <h3 className="font-semibold text-gray-700">Ventas recientes</h3>
          <button onClick={() => navigate('/ventas/checkout')} className="text-xs text-blue-600 font-medium hover:underline">
            Nueva venta →
          </button>
        </div>
        {loading ? (
          <div className="flex items-center justify-center py-12 text-gray-400 text-sm gap-2">
            <div className="animate-spin h-4 w-4 border-2 border-blue-500 border-t-transparent rounded-full" />
            Cargando...
          </div>
        ) : ventas.length === 0 ? (
          <div className="text-center py-12 text-gray-400 text-sm">No hay ventas registradas</div>
        ) : (
          <div className="divide-y divide-gray-100">
            {ventas.slice(0, 8).map((v) => (
              <div key={v.id} className="px-6 py-3.5 flex items-center justify-between hover:bg-gray-50 transition-colors">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                    <ShoppingCart size={14} className="text-blue-600" />
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-700">Venta #{v.id}</p>
                    <p className="text-xs text-gray-400">{v.fecha?.slice(0, 10) ?? '—'}</p>
                  </div>
                </div>
                <div className="flex items-center gap-4">
                  <p className="text-sm font-semibold text-gray-800">S/ {v.total?.toFixed(2) ?? '0.00'}</p>
                  <span className={`text-xs px-2.5 py-0.5 rounded-full font-medium ${
                    v.estado === 'PAGADA'   ? 'bg-green-100 text-green-700' :
                    v.estado === 'ANULADA'  ? 'bg-red-100 text-red-600'    :
                    'bg-amber-100 text-amber-700'
                  }`}>
                    {v.estado}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}