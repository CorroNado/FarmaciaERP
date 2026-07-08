import { useParams, useNavigate }  from 'react-router-dom';
import MainLayout                  from '@/presentation/components/layout/MainLayout';
//import Button                      from '@/presentation/components/ui/Button';
import { useAccessReport }         from '@/presentation/hooks/useAccessReport';
import { formatDate }              from '@/core/utils/formatters';

export default function AccessReportPage() {
  const { id }   = useParams();
  const navigate = useNavigate();
  const { report, loading, error } = useAccessReport(id);

  return (
    <MainLayout>
      <div className="flex items-center justify-between mb-8">
        <div>
          <button onClick={() => navigate('/users')} className="text-sm text-slate-500 hover:text-slate-700 flex items-center gap-1 mb-3">
            ← Volver a usuarios
          </button>
          <h2 className="text-2xl font-bold text-slate-800">Reporte de accesos</h2>
          <p className="text-slate-500 text-sm mt-1">Historial del usuario #{id}</p>
        </div>
      </div>

      {loading && (
        <div className="flex items-center gap-2 text-slate-400 text-sm">
          <svg className="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"/>
          </svg>
          Cargando reporte...
        </div>
      )}

      {error && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
          <span>⚠</span> {error}
        </div>
      )}

      {report && (
        <div className="flex flex-col gap-6">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
            {[
              { label: 'Usuario',       value: report.userName,                  color: 'text-slate-800'   },
              { label: 'Total accesos', value: report.accessCount,               color: 'text-blue-600'    },
              { label: 'Último acceso', value: formatDate(report.lastAccess),    color: 'text-slate-800'   },
            ].map((card) => (
              <div key={card.label} className="bg-white rounded-2xl border border-slate-200 p-6">
                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">{card.label}</p>
                <p className={`text-2xl font-bold ${card.color}`}>{card.value}</p>
              </div>
            ))}
          </div>

          {report.history.length > 0 && (
            <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
              <div className="px-6 py-4 border-b border-slate-100">
                <h3 className="font-semibold text-slate-700">Historial de accesos</h3>
              </div>
              <ul className="divide-y divide-slate-100">
                {report.history.map((entry, i) => (
                  <li key={i} className="px-6 py-3.5 flex items-center justify-between hover:bg-slate-50 transition-colors">
                    <span className="text-sm text-slate-700">{entry.action ?? 'Acceso al sistema'}</span>
                    <span className="text-xs text-slate-400 font-medium">{formatDate(entry.date ?? entry.fecha)}</span>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      )}
    </MainLayout>
  );
}