import React from 'react';

export const PeriodoSelector = ({ selectedPeriod, onChange }) => {
  const years = [2026, 2025, 2024];
  const months = [
    { value: '01', label: 'Enero' }, { value: '02', label: 'Febrero' },
    { value: '03', label: 'Marzo' }, { value: '04', label: 'Abril' },
    { value: '05', label: 'Mayo' }, { value: '06', label: 'Junio' },
    { value: '07', label: 'Julio' }, { value: '08', label: 'Agosto' },
    { value: '09', label: 'Septiembre' }, { value: '10', label: 'Octubre' },
    { value: '11', label: 'Noviembre' }, { value: '12', label: 'Diciembre' }
  ];

  const handleYearChange = (e) => {
    onChange({ ...selectedPeriod, year: e.target.value });
  };

  const handleMonthChange = (e) => {
    onChange({ ...selectedPeriod, month: e.target.value });
  };

  return (
    <div className="flex items-center gap-3 bg-white p-3 rounded-xl border border-slate-200 shadow-sm">
      <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Periodo Fiscal</span>
      <div className="flex items-center gap-2">
        <select
          value={selectedPeriod.year}
          onChange={handleYearChange}
          className="text-sm border border-slate-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-500 bg-slate-50 text-slate-700"
        >
          {years.map(y => (
            <option key={y} value={y}>{y}</option>
          ))}
        </select>
        <select
          value={selectedPeriod.month}
          onChange={handleMonthChange}
          className="text-sm border border-slate-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-500 bg-slate-50 text-slate-700"
        >
          {months.map(m => (
            <option key={m.value} value={m.value}>{m.label}</option>
          ))}
        </select>
      </div>
    </div>
  );
};