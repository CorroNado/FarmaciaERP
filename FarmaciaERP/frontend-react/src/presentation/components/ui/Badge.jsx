export default function Badge({ value }) {
  const styles = {
    ACTIVO:         'bg-emerald-50 text-emerald-700 border border-emerald-200',
    INACTIVO:       'bg-red-50 text-red-600 border border-red-200',
    activo:         'bg-emerald-50 text-emerald-700 border border-emerald-200',
    inactivo:       'bg-red-50 text-red-600 border border-red-200',
    ADMINISTRADOR:  'bg-violet-50 text-violet-700 border border-violet-200',
    USUARIO:        'bg-blue-50 text-blue-700 border border-blue-200',
  };

  const dots = {
    ACTIVO:   'bg-emerald-500',
    INACTIVO: 'bg-red-400',
    activo:   'bg-emerald-500',
    inactivo: 'bg-red-400',
  };

  return (
    <span className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[value] ?? 'bg-slate-100 text-slate-600'}`}>
      {dots[value] && <span className={`w-1.5 h-1.5 rounded-full ${dots[value]}`} />}
      {value}
    </span>
  );
}