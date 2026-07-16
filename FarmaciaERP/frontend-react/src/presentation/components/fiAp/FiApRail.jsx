export const CICLO_FI_AP_FASES = [
  { id: 0, tag: 'Fase 01', title: 'Captura de Excepciones de Facturación', subtitle: 'RN-AP1-01', path: '/fico/fi-ap/excepciones-facturacion' },
  { id: 1, tag: 'Fase 02', title: 'Gestión Humana de Disputas Comerciales', subtitle: 'RN-AP2-01', path: '/fico/fi-ap/disputas-comerciales' },
  { id: 2, tag: 'Fase 03', title: 'Ajustes Contables y Regularización', subtitle: 'RN-AP3-01', path: '/fico/fi-ap/ajustes-contables' },
  { id: 3, tag: 'Fase 04', title: 'Estrategia de Tesorería y Riesgo Sanitario', subtitle: 'RN-AP4-01', path: '/fico/fi-ap/lotes-pago' },
  { id: 4, tag: 'Fase 05', title: 'Procesamiento Automático y Propuesta de Pago', subtitle: 'RN-AP5-01', path: '/fico/fi-ap/propuestas-pago' },
  { id: 5, tag: 'Fase 06', title: 'Dispersión Bancaria y Conciliación de Cierre', subtitle: 'RN-AP6-01', path: '/fico/fi-ap/dispersion-bancaria' },
];

/**
 * Rail de progreso del ciclo Financial Accounting · Accounts Payable
 * (SAP FI-AP). `activeStep` = índice de la fase activa. `maxReached` =
 * hasta qué fase puede navegar el usuario (las siguientes se muestran
 * bloqueadas).
 */
export default function FiApRail({ activeStep = 0, maxReached = 0, onNavigate }) {
  return (
    <div className="rounded-2xl overflow-hidden border border-emerald-900/10 shadow-sm mb-6">
      <div className="bg-[#173C36] px-6 pt-5 pb-4">
        <div className="flex items-baseline justify-between gap-4 flex-wrap mb-4">
          <div className="flex items-baseline gap-3">
            <span className="font-mono text-[10px] tracking-widest uppercase border border-white/25 text-amber-200/90 px-2 py-1 rounded">
              SAP · FI-AP
            </span>
            <div>
              <h1 className="text-white font-semibold text-lg leading-tight">FI-AP — Cuentas por Pagar</h1>
              <p className="font-mono text-[11px] text-emerald-200/80 tracking-wide">Excepciones de Facturación y Pagos a Proveedores</p>
            </div>
          </div>
          <span className="font-mono text-[11px] text-amber-100/70">
            {CICLO_FI_AP_FASES[activeStep]?.tag} — {CICLO_FI_AP_FASES[activeStep]?.title}
          </span>
        </div>

        <div className="flex gap-1.5">
          {CICLO_FI_AP_FASES.map((fase) => {
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
