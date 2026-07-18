import React, { useState, useEffect } from 'react';
import { useContabilidad } from '../../hooks/Contabilidad/useContabilidad';
import { PeriodoSelector } from '../../components/ui/PeriodoSelector';

export const EstadoResultadosPage = () => {
  const { estadoResultados, loading, fetchEstadoResultados } = useContabilidad();
  const [period, setPeriod] = useState({ year: '2026', month: '07' });

  useEffect(() => {
    fetchEstadoResultados(`${period.year}-${period.month}`);
  }, [period, fetchEstadoResultados]);

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Estado de Resultados</h1>
          <p className="text-sm text-slate-500">AnÃ¡lisis de pÃ©rdidas y ganancias acumuladas por el periodo fiscal.</p>
        </div>
        <PeriodoSelector selectedPeriod={period} onChange={setPeriod} />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Ingresos Operacionales</span>
          <span className="text-2xl font-bold text-emerald-600 mt-2">
            S/ {estadoResultados?.totalIngresos?.toFixed(2) || '0.00'}
          </span>
        </div>
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Utilidad Neta</span>
          <span className="text-2xl font-bold text-indigo-600 mt-2">
            S/ {estadoResultados?.utilidadNeta?.toFixed(2) || '0.00'}
          </span>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12 text-slate-400">Generando Estado de Resultados...</div>
      ) : (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 text-xs font-semibold text-slate-500 uppercase border-b border-slate-200">
                <th className="p-4">Concepto Contable</th>
                <th className="p-4 text-right">Monto Acumulado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 text-sm text-slate-700">
              {estadoResultados?.conceptos?.map((c, idx) => (
                <tr key={idx} className={c.esCabecera ? 'font-semibold bg-slate-50/50' : 'hover:bg-slate-50'}>
                  <td className="p-4">{c.descripcion}</td>
                  <td className={`p-4 text-right ${c.monto < 0 ? 'text-red-500' : ''}`}>
                    S/ {c.monto?.toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};