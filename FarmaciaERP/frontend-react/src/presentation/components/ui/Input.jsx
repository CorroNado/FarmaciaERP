export default function Input({
  label,
  name,
  type        = 'text',
  value,
  onChange,
  error,
  placeholder = '',
  disabled    = false,
  className   = '',
}) {
  return (
    <div className="flex flex-col gap-1.5">
      {label && (
        <label htmlFor={name} className="text-xs font-semibold text-slate-600 uppercase tracking-wide">
          {label}
        </label>
      )}
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        className={`
          w-full border rounded-lg px-3.5 py-2.5 text-sm text-slate-800
          placeholder:text-slate-400 outline-none transition-all duration-150
          focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500
          disabled:bg-slate-50 disabled:cursor-not-allowed
          ${error ? 'border-red-400 bg-red-50' : 'border-slate-200 bg-white hover:border-slate-300'}
          ${className}
        `}
      />
      {error && (
        <span className="text-xs text-red-500 flex items-center gap-1">
          <span>⚠</span> {error}
        </span>
      )}
    </div>
  );
}