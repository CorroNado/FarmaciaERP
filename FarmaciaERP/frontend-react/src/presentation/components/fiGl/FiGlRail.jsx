import React from 'react';

export const FiGlRail = ({ activeTab, onTabChange }) => {
  const phases = [
    { id: 'catalogo', label: 'Plan de Cuentas', sub: 'CatÃ¡logo Maestro' },
    { id: 'costos', label: 'Centros de Costo', sub: 'Plan analÃ­tico' },
    { id: 'diario', label: 'Libro Diario', sub: 'Asientos contables' },
    { id: 'balance', label: 'Balance General', sub: 'Estado de situaciÃ³n' },
    { id: 'resultados', label: 'Estado de Resultados', sub: 'Perdidas y Ganancias' },
    { id: 'activos', label: 'Activos Fijos', sub: 'Control & Depreciacion' }
  ];

  return (
    <div className="w-64 bg-slate-900 border-r border-slate-800 text-slate-300 flex flex-col h-full min-h-screen">
      <div className="p-4 border-b border-slate-800">
        <h2 className="text-sm font-bold text-white tracking-wider uppercase">Finanzas FI-GL</h2>
        <span className="text-xs text-slate-400">Contabilidad General</span>
      </div>
      <nav className="flex-1 p-3 space-y-1">
        {phases.map((phase) => (
          <button
            key={phase.id}
            onClick={() => onTabChange(phase.id)}
            type="button"
            className={`w-full text-left p-3 rounded-xl transition-all duration-150 flex flex-col gap-0.5 ${
              activeTab === phase.id
                ? 'bg-indigo-600 text-white font-semibold shadow-md shadow-indigo-600/20'
                : 'hover:bg-slate-800 text-slate-400 hover:text-white'
            }`}
          >
            <span className="text-xs uppercase tracking-wider opacity-60">MODULO</span>
            <span className="text-sm">{phase.label}</span>
            <span className="text-[10px] opacity-80 font-normal">{phase.sub}</span>
          </button>
        ))}
      </nav>
    </div>
  );
};