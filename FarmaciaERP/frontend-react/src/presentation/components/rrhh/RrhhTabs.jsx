import { NavLink } from 'react-router-dom';

export const RRHH_FASES = [
  { tag: 'RRHH.01', title: 'Contratación', path: '/rrhh/contratacion' },
  { tag: 'RRHH.02', title: 'Control de Asistencia', path: '/rrhh/asistencia' },
  { tag: 'RRHH.03', title: 'Nómina / Planilla', path: '/rrhh/planilla' },
];

export default function RrhhTabs() {
  return (
    <div className="flex gap-2 mb-4 flex-wrap">
      {RRHH_FASES.map((fase) => (
        <NavLink
          key={fase.path}
          to={fase.path}
          className={({ isActive }) =>
            [
              'flex items-center gap-2 px-3.5 py-2 rounded-xl border text-sm font-medium transition-all',
              isActive
                ? 'bg-teal-50 border-teal-300 text-teal-800'
                : 'bg-white border-slate-200 text-slate-600 hover:border-slate-300 hover:bg-slate-50',
            ].join(' ')
          }
        >
          <span className="font-mono text-[10px] tracking-widest uppercase text-teal-600">{fase.tag}</span>
          <span>{fase.title}</span>
        </NavLink>
      ))}
    </div>
  );
}
