import Button from './Button';

export default function Modal({
  isOpen,
  title,
  children,
  onClose,
  onConfirm,
  confirmText    = 'Confirmar',
  cancelText     = 'Cancelar',
  confirmVariant = 'primary',
  loading        = false,
}) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" onClick={onClose} />
      <div className="relative bg-white rounded-2xl shadow-2xl w-full max-w-md z-10 overflow-hidden">
        <div className="px-6 py-5 border-b border-slate-100 flex items-center justify-between">
          <h2 className="text-base font-semibold text-slate-800">{title}</h2>
          <button onClick={onClose} className="text-slate-400 hover:text-slate-600 transition-colors w-8 h-8 flex items-center justify-center rounded-lg hover:bg-slate-100">
            ✕
          </button>
        </div>
        <div className="px-6 py-5">{children}</div>
        {onConfirm && (
          <div className="px-6 py-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3">
            <Button variant="outline" onClick={onClose} disabled={loading}>{cancelText}</Button>
            <Button variant={confirmVariant} onClick={onConfirm} loading={loading}>{confirmText}</Button>
          </div>
        )}
      </div>
    </div>
  );
}