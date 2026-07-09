import { NavLink }           from 'react-router-dom';
import { useAuthContext }    from '@/presentation/hooks/useAuthContext';
import { getModulesForRole } from '@/core/utils/roles';
import { ShoppingCart , TriangleAlert, Package, 
  Users, DollarSign, Megaphone,ChartLine, Settings, Lock, House}        from "lucide-react";
import { useLocation } from 'react-router-dom';

const ICONS = {
  dashboard:  <House         size={14}/> ,
  osd:        <ShoppingCart  size={14}/>,
  mm:         <Package       size={14}/>,
  qm:         <TriangleAlert size={14}/>,
  crm:        <Megaphone     size={14}/>,
  fico:       <DollarSign    size={14}/>,
  rrhh:       <Users         size={14}/>,
  pmps:       <Settings      size={14}/>,
  bi:         <ChartLine     size={14}/>,
  seguridad:  <Lock          size={14}/>};

const SUBMODULOS = {
  '/osd': [
    { to: '/osd/checkout',   label: 'Checkout Frontend'  },
    { to: '/osd/partners',   label: 'Business Partners'  },
    { to: '/osd/pricing',    label: 'Pricing Engine'     },
    { to: '/osd/financial',  label: 'Financial & Fiscal' },
    { to: '/osd/logistics',  label: 'Logistics Hub'      },
    { to: '/osd/reverseops', label: 'Reverse Ops'        },
  ],
}; 

export default function Topbar() {
  const { user }    = useAuthContext();
  const location    = useLocation();
  const role        = user?.rol ?? '';
  const modules     = getModulesForRole(role);

  const links = [
    { key: 'dashboard', label: 'Dashboard', to: '/', end: true },
    ...modules.map((m) => ({ key: m.key, label: m.label, to: m.to, end: false })),
  ];

  // Detecta si está en un módulo con subpáginas
  const moduloActivo = Object.keys(SUBMODULOS).find((path) =>
    location.pathname.startsWith(path)
  );
  const sublinks = moduloActivo ? SUBMODULOS[moduloActivo] : [];

  return (
    <div className="bg-white border-b border-gray-200 shrink-0">
      {/* Navegación principal */}
      <div className="px-6">
        <div className="flex items-center gap-0 overflow-x-auto" style={{ msOverflowStyle: 'none', scrollbarWidth: 'none' }}>
          {links.map((link) => (
            <NavLink
              key={link.key}
              to={link.to}
              end={link.end}
              className={({ isActive }) =>
                `flex items-center gap-2 px-4 py-3 border-b-2 transition whitespace-nowrap ${
                  isActive
                    ? 'border-blue-500 text-blue-600 bg-blue-50'
                    : 'border-transparent text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                }`
              }
            >
              <span>{ICONS[link.key]}</span>
              <span className="font-semibold text-sm">{link.label}</span>
            </NavLink>
          ))}
        </div>
      </div>

      {/* Subnavegación — solo cuando hay subpáginas */}
      {sublinks.length > 0 && (
        <div className="px-6 bg-gray-50 border-t border-gray-100">
          <div className="flex items-center gap-1 overflow-x-auto" style={{ msOverflowStyle: 'none', scrollbarWidth: 'none' }}>
            {sublinks.map((sub) => (
              <NavLink
                key={sub.to}
                to={sub.to}
                className={({ isActive }) =>
                  `px-4 py-2 text-xs font-medium border-b-2 transition whitespace-nowrap ${
                    isActive
                      ? 'border-blue-500 text-blue-600 bg-blue-50'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:bg-gray-100'
                  }`
                }
              >
                {sub.label}
              </NavLink>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}