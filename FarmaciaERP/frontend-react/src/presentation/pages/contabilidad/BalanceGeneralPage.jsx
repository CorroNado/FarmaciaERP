import React, { useState, useEffect } from 'react';
import { useContabilidad } from '../../hooks/useContabilidad';
import { PeriodoSelector } from '../../components/ui/PeriodoSelector';

export const BalanceGeneralPage = () => {
  const { balanceGeneral, loading, fetchBalanceGeneral } = useContabilidad();
  const [period, setPeriod] = useState({ year: '2026', month: '07' });

  useEffect(() => {
    fetchBalanceGeneral(`${period.year}-${period.month}`);
  }, [period, fetchBalanceGeneral]);

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Balance General</h1>
          <p className="text-sm text-slate-500">Estado de situaciÃ³n financiera de la farmacia en tiempo real.</p>
        </div>
        <PeriodoSelector selectedPeriod={period} onChange={setPeriod} />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Activos Totales</span>
          <span className="text-2xl font-bold text-slate-800 mt-2">
            S/ {balanceGeneral?.totalActivo?.toFixed(2) || '0.00'}
          </span>
        </div>
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Pasivos Totales</span>
          <span className="text-2xl font-bold text-emerald-600 mt-2">
            S/ {balanceGeneral?.totalPasivo?.toFixed(2) || '0.00'}
          </span>
        </div>
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Patrimonio Neto</span>
          <span className="text-2xl font-bold text-indigo-600 mt-2">
            S/ {balanceGeneral?.totalPatrimonio?.toFixed(2) || '0.00'}
          </span>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12 text-slate-400">Procesando estructura del Balance...</div>
      ) : (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 text-xs font-semibold text-slate-500 uppercase border-b border-slate-200">
                <th className="p-4">Cuenta / Rubro</th>
                <th className="p-4 text-right">Saldo Periodo</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 text-sm text-slate-700">
              {balanceGeneral?.partidas?.map((p, idx) => (
                <tr key={idx} className={p.esCabecera ? 'font-semibold bg-slate-50/50' : 'hover:bg-slate-50'}>
                  <td className="p-4">{p.descripcion}</td>
                  <td className={`p-4 text-right ${p.monto < 0 ? 'text-red-500' : ''}`}>
                    S/ {p.monto?.toFixed(2)}
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