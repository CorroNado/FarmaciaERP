export default function Select({
  label,
  name,
  value,
  onChange,
  options     = [],
  error,
  disabled    = false,
  placeholder = 'Seleccionar...',
  className   = '',
}) {
  return (
    <div className="flex flex-col gap-1.5">
      {label && (
        <label htmlFor={name} className="text-xs font-semibold text-slate-600 uppercase tracking-wide">
          {label}
        </label>
      )}
      <select
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        disabled={disabled}
        className={`
          w-full border rounded-lg px-3.5 py-2.5 text-sm text-slate-800
          outline-none transition-all duration-150 cursor-pointer
          focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500
          disabled:bg-slate-50 disabled:cursor-not-allowed
          ${error ? 'border-red-400 bg-red-50' : 'border-slate-200 bg-white hover:border-slate-300'}
          ${className}
        `}
      >
        <option value="">{placeholder}</option>
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>
      {error && <span className="text-xs text-red-500">⚠ {error}</span>}
    </div>
  );
}