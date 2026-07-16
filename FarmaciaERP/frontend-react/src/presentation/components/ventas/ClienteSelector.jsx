import { X, UserPlus } from 'lucide-react';
import Button from '@/presentation/components/ui/Button';

// Selector de cliente compartido entre Punto de Venta y Cotizaciones (SD.01.01).
export default function ClienteSelector({
  cliente, dniInput, setDniInput, onBuscarDni, onAbrirNuevoCliente, limpiarCliente,
}) {
  return (
    <div className="bg-white border border-slate-200 rounded-2xl p-6">
      <h2 className="text-lg font-bold text-slate-800 mb-4">Cliente</h2>
      {cliente ? (
        <div className="flex items-center justify-between bg-emerald-50 border border-emerald-200 rounded-xl px-4 py-3">
          <div>
            <div className="text-sm font-semibold text-emerald-800">{cliente.nombreCompleto}</div>
            <div className="text-xs text-emerald-600 font-mono">DNI {cliente.dni}</div>
          </div>
          <button onClick={limpiarCliente} className="text-emerald-500 hover:text-emerald-700">
            <X size={16} />
          </button>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          <div className="flex gap-2">
            <input
              value={dniInput}
              onChange={(e) => setDniInput(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && onBuscarDni()}
              placeholder="Buscar por DNI (8 dígitos)"
              className="flex-1 border border-slate-200 rounded-lg px-3.5 py-2.5 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500"
            />
            <Button variant="secondary" onClick={onBuscarDni}>Buscar</Button>
          </div>
          <button
            onClick={onAbrirNuevoCliente}
            className="flex items-center justify-center gap-2 text-sm text-blue-600 hover:bg-blue-50 rounded-lg py-2 transition"
          >
            <UserPlus size={14} /> Registrar nuevo cliente
          </button>
        </div>
      )}
    </div>
  );
}
