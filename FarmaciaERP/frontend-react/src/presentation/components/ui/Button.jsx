export default function Button({
  children,
  type      = 'button',
  variant   = 'primary',
  loading   = false,
  disabled  = false,
  onClick,
  className = '',
}) {
  const base = 'inline-flex items-center justify-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all duration-150 disabled:opacity-40 disabled:cursor-not-allowed';

  const variants = {
    primary:   'bg-blue-600 text-white hover:bg-blue-700 shadow-sm shadow-blue-200',
    secondary: 'bg-slate-100 text-slate-700 hover:bg-slate-200',
    danger:    'bg-red-600 text-white hover:bg-red-700 shadow-sm shadow-red-200',
    ghost:     'text-blue-600 hover:bg-blue-50',
    outline:   'border border-slate-300 text-slate-700 hover:bg-slate-50',
  };

  return (
    <button
      type={type}
      disabled={disabled || loading}
      onClick={onClick}
      className={`${base} ${variants[variant]} ${className}`}
    >
      {loading ? (
        <>
          <svg className="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"/>
          </svg>
          Procesando...
        </>
      ) : children}
    </button>
  );
}