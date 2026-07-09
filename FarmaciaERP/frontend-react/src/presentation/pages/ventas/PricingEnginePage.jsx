
import { TrendingUp, Tag, Plus, Clock } from 'lucide-react';

const LISTAS_B2B = [
  { id: 1, nombre: 'Lista Corporativa Premium', desc: 'Clientes con facturación > $100K/año', estado: 'Activa', descuentos: [{ rango: '1-100 unidades', valor: 'Precio Base' }, { rango: '101-500 unidades', valor: '-5% Descuento' }, { rango: '>500 unidades', valor: '-10% Descuento' }], clientes: ['Dist. Farm. Los Andes', 'Droguería Peruana', '+1 más'] },
  { id: 2, nombre: 'Contrato Marco - Sector Público', desc: 'Licitaciones gubernamentales', estado: 'Activa', descuentos: [{ rango: 'Descuento General', valor: '-12% Precio Lista' }, { rango: 'Plazo de Pago', valor: '60 días' }, { rango: 'Vigencia', valor: '01/01/2026 - 31/12/2026' }], clientes: [] },
];

const PROMOS_B2C = [
  { id: 1, nombre: '2x1 Vitaminas - Fin de Semana', desc: 'Productos seleccionados de vitaminas y suplementos', estado: 'Programada', estadoColor: 'bg-violet-100 text-violet-700', vigencia: '27/05/2026 - 28/05/2026', mecanica: 'Lleva 2, Paga 1 (50% desc)', tags: ['Vitamina C 1000mg', 'Multivitamínico', '+8 más'] },
  { id: 2, nombre: 'Cupón CYBER - 15% OFF', desc: 'Aplicable en compras online superiores a $100', estado: 'Activa', estadoColor: 'bg-green-100 text-green-700', codigo: 'CYBER15', validoHasta: '31/05/2026', usos: '248 / 1000' },
  { id: 3, nombre: 'Flash Sale - Dermatología', desc: 'Solo por 6 horas - Categoría Cuidado de la Piel', estado: 'LIVE', estadoColor: 'bg-red-500 text-white', descuento: 'Hasta -30%', tiempo: '02:34:18' },
];

export default function PricingEnginePage() {
  return (
    <div>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-gray-900">Omni-Pricing Engine - Consola de Precios y Campañas</h2>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">

        {/* B2B */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 font-semibold text-gray-700">
              <TrendingUp size={16} className="text-blue-600" />
              Pricing B2B - Listas por Volumen
            </div>
            <button className="bg-blue-600 text-white text-xs px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-blue-700 transition">
              <Plus size={12} /> Nueva Lista
            </button>
          </div>
          <div className="flex flex-col gap-4">
            {LISTAS_B2B.map((lista) => (
              <div key={lista.id} className="border border-gray-100 rounded-xl p-4">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-sm font-semibold text-gray-700">{lista.nombre}</p>
                  <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded-full">{lista.estado}</span>
                </div>
                <p className="text-xs text-gray-400 mb-3">{lista.desc}</p>
                <div className="flex flex-col gap-1 mb-3">
                  {lista.descuentos.map((d) => (
                    <div key={d.rango} className="flex justify-between text-xs">
                      <span className="text-gray-500">{d.rango}:</span>
                      <span className={`font-semibold ${d.valor.includes('%') ? 'text-green-600' : 'text-gray-700'}`}>{d.valor}</span>
                    </div>
                  ))}
                </div>
                {lista.clientes.length > 0 && (
                  <div className="flex flex-wrap gap-1">
                    {lista.clientes.map((c) => (
                      <span key={c} className="text-xs bg-blue-50 text-blue-700 px-2 py-0.5 rounded-full">{c}</span>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* B2C */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 font-semibold text-gray-700">
              <Tag size={16} className="text-violet-600" />
              Promociones B2C - Retail & E-commerce
            </div>
            <button className="bg-violet-600 text-white text-xs px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-violet-700 transition">
              <Plus size={12} /> Nueva Promo
            </button>
          </div>
          <div className="flex flex-col gap-4">
            {PROMOS_B2C.map((promo) => (
              <div key={promo.id} className="border border-gray-100 rounded-xl p-4">
                <div className="flex items-center justify-between mb-1">
                  <p className="text-sm font-semibold text-gray-700">{promo.nombre}</p>
                  <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${promo.estadoColor}`}>{promo.estado}</span>
                </div>
                <p className="text-xs text-gray-400 mb-3">{promo.desc}</p>
                {promo.vigencia && (
                  <div className="text-xs text-gray-500 flex items-center gap-1 mb-1">
                    <Clock size={11} /> Vigencia: <span className="font-medium text-gray-700">{promo.vigencia}</span>
                  </div>
                )}
                {promo.mecanica && <p className="text-xs text-gray-500 mb-2">Mecánica: <span className="font-medium text-violet-600">{promo.mecanica}</span></p>}
                {promo.codigo && <p className="text-xs text-gray-500 mb-1">Código: <span className="font-mono font-bold bg-gray-100 px-2 py-0.5 rounded">{promo.codigo}</span></p>}
                {promo.usos && <p className="text-xs text-gray-500 mb-1">Usos: <span className="font-medium text-blue-600">{promo.usos}</span></p>}
                {promo.descuento && <p className="text-xs text-gray-500">Descuento: <span className="font-semibold text-red-500">{promo.descuento}</span></p>}
                {promo.tiempo && <p className="text-xs font-mono font-bold text-red-500 mt-1">{promo.tiempo}</p>}
                {promo.tags && (
                  <div className="flex flex-wrap gap-1 mt-2">
                    {promo.tags.map((t) => (
                      <span key={t} className="text-xs bg-violet-50 text-violet-700 px-2 py-0.5 rounded-full">{t}</span>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}