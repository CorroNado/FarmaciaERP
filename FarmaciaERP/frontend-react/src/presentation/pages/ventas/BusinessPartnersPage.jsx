import { useState } from 'react';
import { Building2, Users, RefreshCw, Plus, Search } from 'lucide-react';
import { useClientes } from '@/presentation/hooks/useClientes';

function getSeguroColor(tipoSeguro) {
  switch (tipoSeguro) {
    case 'ESSALUD':    return 'bg-blue-100 text-blue-700';
    case 'SIS':        return 'bg-green-100 text-green-700';
    case 'SIN_SEGURO': return 'bg-gray-100 text-gray-700';
    default:           return 'bg-yellow-100 text-yellow-700';
  }
}

function ClienteCard({ c }) {
  return (
    <div className="py-3">
      <div className="flex items-center justify-between mb-1">
        <p className="text-sm font-semibold text-gray-700">{c.nombres?.value}</p>
        <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${getSeguroColor(c.tipoSeguro)}`}>
          {c.tipoSeguro}
        </span>
      </div>
      <p className="text-xs text-gray-400">DNI: {c.dni?.dni}</p>
    </div>
  );
}

function Paginacion({ pagina, total, onChange }) {
  return (
    <div className="flex items-center justify-between mt-4">
      <button
        onClick={() => onChange((p) => Math.max(p - 1, 1))}
        disabled={pagina === 1}
        className="px-3 py-1.5 border border-gray-200 rounded-lg text-xs disabled:opacity-50 hover:bg-gray-50 transition"
      >
        Anterior
      </button>
      <span className="text-xs text-gray-500">Página {pagina} de {total || 1}</span>
      <button
        onClick={() => onChange((p) => Math.min(p + 1, total))}
        disabled={pagina >= total}
        className="px-3 py-1.5 border border-gray-200 rounded-lg text-xs disabled:opacity-50 hover:bg-gray-50 transition"
      >
        Siguiente
      </button>
    </div>
  );
}

export default function BusinessPartnersPage() {
  const { clientes, loading } = useClientes();

  const [searchB2C, setSearchB2C] = useState('');
  const [searchB2B, setSearchB2B] = useState('');
  const [paginaB2C, setPaginaB2C] = useState(1);
  const [paginaB2B, setPaginaB2B] = useState(1);

  const porPagina = 5;

  // B2C — todos excepto ESSALUD
  const clientesB2C = (clientes || []).filter((c) => c.tipoSeguro !== 'ESSALUD');
  const filteredB2C = clientesB2C.filter((c) => {
    const nombre = c.nombres?.value?.toLowerCase() ?? '';
    const dni    = c.dni?.dni ?? '';
    return nombre.includes(searchB2C.toLowerCase()) || dni.includes(searchB2C);
  });
  const paginasB2C        = Math.ceil(filteredB2C.length / porPagina);
  const clientesB2CPagina = filteredB2C.slice((paginaB2C - 1) * porPagina, paginaB2C * porPagina);

  // B2B — solo ESSALUD
  const clientesB2B = (clientes || []).filter((c) => c.tipoSeguro === 'ESSALUD');
  const filteredB2B = clientesB2B.filter((c) => {
    const nombre = c.nombres?.value?.toLowerCase() ?? '';
    const dni    = c.dni?.dni ?? '';
    return nombre.includes(searchB2B.toLowerCase()) || dni.includes(searchB2B);
  });
  const paginasB2B        = Math.ceil(filteredB2B.length / porPagina);
  const clientesB2BPagina = filteredB2B.slice((paginaB2B - 1) * porPagina, paginaB2B * porPagina);

  return (
    <div>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-gray-900">OSD Core Manager - Panel de Control de Datos Maestros</h2>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">

        {/* B2C — Consumidores */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 font-semibold text-gray-700">
              <Users size={16} className="text-violet-600" />
              Clientes - Consumidores
            </div>
            <button className="bg-violet-600 text-white text-xs px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-violet-700 transition">
              <Plus size={12} /> Nuevo Cliente
            </button>
          </div>
          <div className="relative mb-3">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              value={searchB2C}
              onChange={(e) => { setSearchB2C(e.target.value); setPaginaB2C(1); }}
              placeholder="Buscar por DNI o nombre..."
              className="w-full pl-9 pr-4 py-2.5 border border-gray-200 rounded-xl text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
            />
          </div>
          <div className="divide-y divide-gray-100">
            {loading ? (
              <p className="text-sm text-gray-400 py-4">Cargando clientes...</p>
            ) : clientesB2CPagina.length === 0 ? (
              <p className="text-sm text-gray-400 py-4 text-center">Sin resultados</p>
            ) : (
              clientesB2CPagina.map((c) => <ClienteCard key={c.id} c={c} />)
            )}
          </div>
          <Paginacion pagina={paginaB2C} total={paginasB2C} onChange={setPaginaB2C} />
        </div>

        {/* B2B — Corporativos ESSALUD */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 font-semibold text-gray-700">
              <Building2 size={16} className="text-blue-600" />
              Corporativos - EsSalud
            </div>
            <button className="bg-blue-600 text-white text-xs px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-blue-700 transition">
              <Plus size={12} /> Nuevo Corporativo
            </button>
          </div>
          <div className="relative mb-3">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              value={searchB2B}
              onChange={(e) => { setSearchB2B(e.target.value); setPaginaB2B(1); }}
              placeholder="Buscar por DNI o nombre..."
              className="w-full pl-9 pr-4 py-2.5 border border-gray-200 rounded-xl text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
            />
          </div>
          <div className="divide-y divide-gray-100">
            {loading ? (
              <p className="text-sm text-gray-400 py-4">Cargando...</p>
            ) : clientesB2BPagina.length === 0 ? (
              <p className="text-sm text-gray-400 py-4 text-center">Sin clientes EsSalud</p>
            ) : (
              clientesB2BPagina.map((c) => <ClienteCard key={c.id} c={c} />)
            )}
          </div>
          <Paginacion pagina={paginaB2B} total={paginasB2B} onChange={setPaginaB2B} />
        </div>

      </div>

      {/* Sincronizador */}
      <div className="bg-white rounded-2xl border border-gray-200 p-5">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-2 font-semibold text-gray-700">
            <RefreshCw size={16} className="text-green-600" />
            Sincronizador de Catálogo
          </div>
          <button className="bg-green-500 text-white text-xs px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-green-600 transition">
            Sincronizar Ahora
          </button>
        </div>
        <div className="grid grid-cols-3 gap-4">
          {[
            { label: 'Productos Sincronizados', value: '12,458', sub: 'Última sincronización: Hace 2 horas', color: 'text-blue-600'  },
            { label: 'Nuevos Productos',        value: '24',     sub: 'Pendientes de revisión',              color: 'text-green-600' },
            { label: 'Productos Actualizados',  value: '156',    sub: 'Cambios de precio o stock',           color: 'text-amber-600' },
          ].map((s) => (
            <div key={s.label} className="bg-gray-50 rounded-xl p-4">
              <p className="text-xs text-gray-500 mb-1">{s.label}</p>
              <p className={`text-2xl font-bold ${s.color}`}>{s.value}</p>
              <p className="text-xs text-gray-400 mt-1">{s.sub}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}