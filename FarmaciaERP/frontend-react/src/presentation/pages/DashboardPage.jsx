import { useNavigate }   from 'react-router-dom';
import { useUsers }      from '@/presentation/hooks/useUsers';
import { useAuthContext } from '@/presentation/hooks/useAuthContext';
import { getModulesForRole } from '@/core/utils/roles';
import {ShoppingCart ,Package,TriangleAlert,Megaphone,DollarSign
  ,Users,Settings,ChartLine,Lock
} from "lucide-react";

const MODULE_ICONS = {
  osd:        <ShoppingCart/>,
  mm:         <Package/>,
  qm:         <TriangleAlert/>,
  crm:        <Megaphone/>,
  fico:       <DollarSign/>,
  rrhh:       <Users/>,
  pmps:       <Settings/>,
  bi:         <ChartLine/>,
  seguridad:  <Lock/>};

const MODULE_COLORS = {
  osd:      { bg: 'bg-gradient-to-br from-blue-500    to-blue-600',    border: 'border-blue-100',     icon: 'text-white',    hover: 'hover:border-blue-300   hover:bg-blue-100'    },
  mm:       { bg: 'bg-gradient-to-br from-green-500   to-green-600',   border: 'border-green-100',    icon: 'text-white',    hover: 'hover:border-green-300  hover:bg-green-100'   },
  qm:       { bg: 'bg-gradient-to-br from-purple-500  to-purple-600',  border: 'border-purple-100',   icon: 'text-white',    hover: 'hover:border-purple-300 hover:bg-purple-100'  },
  crm:      { bg: 'bg-gradient-to-br from-pink-500    to-pink-600',    border: 'border-pink-100',     icon: 'text-white',    hover: 'hover:border-pink-300   hover:bg-pink-100'    },
  fico:     { bg: 'bg-gradient-to-br from-amber-500   to-amber-600',   border: 'border-amber-100',    icon: 'text-white',    hover: 'hover:border-amber-300  hover:bg-amber-100'   },
  rrhh:     { bg: 'bg-gradient-to-br from-teal-500    to-teal-600',    border: 'border-teal-100',     icon: 'text-white',    hover: 'hover:border-teal-300   hover:bg-teal-100'    },
  pmps:     { bg: 'bg-gradient-to-br from-orange-500  to-orange-600',  border: 'border-orange-200',   icon: 'text-white',    hover: 'hover:border-orange-300 hover:bg-orange-100'  },
  bi:       { bg: 'bg-gradient-to-br from-indigo-500  to-indigo-600',  border: 'border-indigo-100',   icon: 'text-white',    hover: 'hover:border-indigo-300 hover:bg-indigo-100'  },
  seguridad:{ bg: 'bg-gradient-to-br from-gray-700    to-gray-800',    border: 'border-gray-100',     icon: 'text-white',    hover: 'hover:border-gray-300   hover:bg-gray-100'    },
};

export default function DashboardPage() {
  const navigate              = useNavigate();
  const { user }              = useAuthContext();
  const { users, loading }    = useUsers();
  const modules               = getModulesForRole(user?.rol ?? '');

  const hora = new Date().getHours();
  const saludo = hora < 12 ? 'Buenos días' : hora < 18 ? 'Buenas tardes' : 'Buenas noches';

  return (
    <>

      {/* Bienvenida */}
      <div className="mb-8">
        <p className="text-slate-500 text-sm">{saludo},</p>
        <h2 className="text-2xl font-bold text-slate-800">
          {user?.nombre ? `${user.nombre} ${user.apellido}` : 'Bienvenido'}
        </h2>
        <p className="text-slate-400 text-sm mt-1">
          {new Date().toLocaleDateString('es-PE', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
        </p>
      </div>

      {/* Módulos */}
      <div className="mb-4 flex items-center justify-between">
        <h3 className="text-base font-semibold text-slate-700">Mis módulos</h3>
        <span className="text-xs text-slate-400">{modules.length} disponibles</span>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
        {modules.map((mod) => {
          const color = MODULE_COLORS[mod.key] ?? MODULE_COLORS.seguridad;
          return (
            <button
              key={mod.key}
              onClick={() => navigate(mod.to)}
              className={`
                flex flex-col items-center gap-4 p-5 rounded-2xl border
                ${color.bg} ${color.border} ${color.hover}
                transition-all duration-200 cursor-pointer text-left
                hover:shadow-md group
              `}
            >
              <div className={`${color.icon} transition-transform duration-200 group-hover:scale-110`}>
                {MODULE_ICONS[mod.key]}
              </div>
              <div>
                <p className="text-sm font-semibold text-white leading-tight">{mod.label}</p>               
              </div>
            </button>
          );
        })}
      </div>

      {/* Usuarios recientes — solo ADMINISTRADOR */}
      {user?.rol === 'ADMINISTRADOR' && !loading && users.length > 0 && (
        <div className="mt-8 bg-white rounded-2xl border border-slate-200 overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-100 flex items-center justify-between">
            <h3 className="font-semibold text-slate-700 text-sm">Usuarios recientes</h3>
            <button
              onClick={() => navigate('/usuarios')}
              className="text-xs text-blue-600 hover:text-blue-700 font-medium"
            >
              Ver todos →
            </button>
          </div>
          <div className="divide-y divide-slate-100">
            {users.slice(0, 5).map((u) => (
              <div key={u.id} className="px-6 py-3.5 flex items-center justify-between hover:bg-slate-50 transition-colors">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 bg-slate-200 rounded-full flex items-center justify-center text-slate-600 font-semibold text-xs shrink-0">
                    {u.nombre?.charAt(0)}{u.apellido?.charAt(0)}
                  </div>
                  <div>
                    <p className="text-sm font-medium text-slate-700">{u.nombre} {u.apellido}</p>
                    <p className="text-xs text-slate-400">{u.email}</p>
                  </div>
                </div>
                <span className={`text-xs px-2.5 py-0.5 rounded-full font-medium border ${
                  u.estado === 'ACTIVO'
                    ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                    : 'bg-red-50 text-red-600 border-red-200'
                }`}>
                  {u.estado}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

    </>
  );
}