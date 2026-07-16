export const BLOQUE_A_FASES = [
  { id: 0, tag: 'Fase 01', title: 'Proveedor y Convenio', subtitle: 'LOG.00 / LOG.02', path: '/mm/proveedores' },
  { id: 1, tag: 'Fase 02', title: 'Solicitud de Pedido (MRP)', subtitle: 'LOG.03', path: '/mm/solped' },
  { id: 2, tag: 'Fase 03', title: 'Orden de Compra', subtitle: 'LOG.04', path: '/mm/orden-compra' },
  { id: 3, tag: 'Fase 04', title: 'Entrada de Mercancía (MIGO)', subtitle: 'LOG.05', path: '/mm/migo' },
  { id: 4, tag: 'Fase 05', title: 'Inspección de Calidad (QA11)', subtitle: 'LOG.06', path: '/mm/inspeccion-calidad' },
  { id: 5, tag: 'Fase 06', title: 'Stock y Distribución (STO)', subtitle: 'LOG.07', path: '/mm/distribucion' },
  { id: 6, tag: 'Fase 07', title: 'Verificación de Factura (MIRO)', subtitle: 'LOG.07', path: '/mm/miro' },
  { id: 7, tag: 'Fase 08', title: 'Conciliación 3 vías (MRBR)', subtitle: 'LOG.08', path: '/mm/conciliacion' },
  { id: 8, tag: 'Fase 09', title: 'Ejecución del Pago (F110)', subtitle: 'LOG.09', path: '/mm/pago' },
];

/**
 * Rail de progreso del proceso Procure-to-Pay (estilo SAP MM).
 * `activeStep` = índice de la fase activa. `maxReached` = hasta qué fase
 * puede navegar el usuario (las siguientes se muestran bloqueadas).
 */
export default function LogisticaRail({ activeStep = 0, maxReached = 0, onNavigate }) {
  return (
    <div className="rounded-2xl overflow-hidden border border-teal-900/10 shadow-sm mb-6">
      <div className="bg-[#0a3d3a] px-6 pt-5 pb-4">
        <div className="flex items-baseline justify-between gap-4 flex-wrap mb-4">
          <div className="flex items-baseline gap-3">
            <span className="font-mono text-[10px] tracking-widest uppercase border border-white/25 text-amber-200/90 px-2 py-1 rounded">
              SAP · MM
            </span>
            <div>
              <h1 className="text-white font-semibold text-lg leading-tight">Logística — Procure to Pay</h1>
              <p className="font-mono text-[11px] text-teal-200/80 tracking-wide">Bloque A — Cimientos</p>
            </div>
          </div>
          <span className="font-mono text-[11px] text-amber-100/70">
            {BLOQUE_A_FASES[activeStep]?.tag} — {BLOQUE_A_FASES[activeStep]?.title}
          </span>
        </div>

        <div className="flex gap-1.5">
          {BLOQUE_A_FASES.map((fase) => {
            const done = fase.id < activeStep;
            const active = fase.id === activeStep;
            const reachable = fase.id <= maxReached;
            return (
              <button
                key={fase.id}
                type="button"
                disabled={!reachable}
                onClick={() => reachable && onNavigate?.(fase)}
                className={[
                  'flex-1 min-w-[120px] text-left rounded-lg px-3 py-2.5 transition-all font-mono',
                  active ? 'bg-white/15 border border-white/35' :
                  done   ? 'bg-amber-400/15 border border-amber-300/30 hover:bg-amber-400/20' :
                           'bg-transparent border border-transparent',
                  !reachable ? 'opacity-35 cursor-not-allowed' : 'cursor-pointer',
                ].join(' ')}
              >
                <div className={`text-[10px] tracking-widest ${done ? 'text-amber-300' : active ? 'text-white' : 'text-teal-200/60'}`}>
                  {fase.tag}{done && ' ✓'}
                </div>
                <div className="text-[11.5px] font-sans font-semibold text-teal-50 mt-0.5 leading-snug">
                  {fase.title}
                </div>
                <div className="h-[3px] bg-white/10 rounded mt-2 overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-amber-400 to-amber-200 transition-all duration-300"
                    style={{ width: done ? '100%' : active ? '45%' : '0%' }}
                  />
                </div>
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}
