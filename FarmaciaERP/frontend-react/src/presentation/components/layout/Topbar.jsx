import { NavLink }           from 'react-router-dom';
import { useAuthContext }    from '@/presentation/hooks/Seguridad/useAuthContext';
import { getModulesForRole } from '@/core/utils/roles';
import { ShoppingCart , TriangleAlert, Package, 
  Users, DollarSign, Megaphone,ChartLine, Settings, Lock, House}        from "lucide-react";

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


export default function Topbar() {
  const { user }  = useAuthContext();
  const role      = user?.rol ?? '';
  const modules   = getModulesForRole(role);

  const links = [
    { key: 'dashboard', label: 'Dashboard', to: '/', end: true },
    ...modules.map((m) => ({ key: m.key, label: m.label, to: m.to, end: false })),
  ];

  return (
    <div className="bg-white border-b border-gray-200 px-6 shrink-0">
      <div className="flex items-center gap-0 overflow-x-auto bg-white" style={{ msOverflowStyle: 'none', scrollbarWidth: 'none' }}>
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
            <span className="shrink-0">{ICONS[link.key]}</span>
            <span className="font-semibold text-xs flex items-center gap-1">
              {link.label}
            </span>
          </NavLink>
        ))}
      </div>
    </div>
  );
}