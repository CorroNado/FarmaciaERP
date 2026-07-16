export const CICLO_FI_AR_FASES = [
  { id: 0, tag: 'Fase 01', title: 'Recepción y Auditoría del Cierre de Venta', subtitle: 'POS-SD', path: '/fico/cierre-caja' },
  { id: 1, tag: 'Fase 02', title: 'Contabilización y Declaración de Valores', subtitle: 'RN-AR2-01', path: '/fico/contabilizacion' },
  { id: 2, tag: 'Fase 03', title: 'Auditoría Médica e Impugnación de Recetas', subtitle: 'RN-AR3-01', path: '/fico/recetas' },
  { id: 3, tag: 'Fase 04', title: 'Conciliación de Débitos y Ajustes Técnicos', subtitle: 'RN-AR4-01', path: '/fico/debitos' },
  { id: 4, tag: 'Fase 05', title: 'Procesamiento de Cobros e Imputación Bancaria', subtitle: 'RN-AR5-01', path: '/fico/cobros' },
  { id: 5, tag: 'Fase 06', title: 'Compensación Final y Análisis de Margen Neto', subtitle: 'RN-AR6-01', path: '/fico/compensacion-final' },
];

/**
 * Rail de progreso del ciclo Financial Accounting · Accounts Receivable
 * (SAP FI-AR). `activeStep` = índice de la fase activa. `maxReached` =
 * hasta qué fase puede navegar el usuario (las siguientes se muestran
 * bloqueadas).
 */
export default function FiArRail({ activeStep = 0, maxReached = 0, onNavigate }) {
  return (
    <div className="rounded-2xl overflow-hidden border border-emerald-900/10 shadow-sm mb-6">
      <div className="bg-[#173C36] px-6 pt-5 pb-4">
        <div className="flex items-baseline justify-between gap-4 flex-wrap mb-4">
          <div className="flex items-baseline gap-3">
            <span className="font-mono text-[10px] tracking-widest uppercase border border-white/25 text-amber-200/90 px-2 py-1 rounded">
              SAP · FI-AR
            </span>
            <div>
              <h1 className="text-white font-semibold text-lg leading-tight">FI-AR — Cuentas por Cobrar</h1>
              <p className="font-mono text-[11px] text-emerald-200/80 tracking-wide">Ciclo de Cuentas por Cobrar · Sucursal Farmacia</p>
            </div>
          </div>
          <span className="font-mono text-[11px] text-amber-100/70">
            {CICLO_FI_AR_FASES[activeStep]?.tag} — {CICLO_FI_AR_FASES[activeStep]?.title}
          </span>
        </div>

        <div className="flex gap-1.5">
          {CICLO_FI_AR_FASES.map((fase) => {
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
                <div className={`text-[10px] tracking-widest ${done ? 'text-amber-300' : active ? 'text-white' : 'text-emerald-200/60'}`}>
                  {fase.tag}{done && ' ✓'}
                </div>
                <div className="text-[11.5px] font-sans font-semibold text-emerald-50 mt-0.5 leading-snug">
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
