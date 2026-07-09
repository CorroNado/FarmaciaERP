import { useAuth } from '@/presentation/hooks/useAuth';
import {Pill,Search,Bell,LogOut} from "lucide-react";

export default function Navbar() {
  const { logout, user } = useAuth();
  const role       = user?.rol ?? '';
  
  const initials = user
    ? `${user.nombre?.charAt(0) ?? ''}${user.apellido?.charAt(0) ?? ''}`.toUpperCase()
    : '?';

  const fullName = user?.nombre && user?.apellido
    ? `${user.nombre} ${user.apellido}`
    : user?.email ?? '';

  return (
    <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-8 shrink-0">
      {/*Logo y Nombre*/}
      <div className="flex items-center gap-4">
        <div className="w-11 h-11 bg-gradient-to-br from-blue-500 to-green-500 rounded-xl flex items-center justify-center shadow-lg">
          <Pill className="w-6 h-6 text-white" />
        </div>
        <div>
          <h1 className="text-xl font-bold text-gray-900">FarmaControl</h1>
          <p className="text-xs text-gray-500">Sistema de Gestión Farmacéutica</p>
        </div>
      </div>
       {/* Barra de búsqueda central */}
            <div className="flex-1 max-w-xl mx-8">
              <div className="relative">
                <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  placeholder="Buscar medicamento, cliente o receta..."
                  className="w-full pl-10 pr-4 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition text-sm"
                />
              </div>
            </div>
      <div className="flex items-center gap-4">
        <button className="relative p-2 hover:bg-gray-100 rounded-lg transition">
                <Bell className="w-5 h-5 text-gray-600" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
              </button>
        {/* Usuario actual */}
        {user && (
          <div className="flex items-center gap-2.5">
            <div className="w-8 h-8 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full flex items-center justify-center text-white font-semibold text-xs">
              {initials}
            </div>
            <div className="flex flex-col">
              <span className="text-xs font-semibold text-gray-900">
                {fullName}
              </span>
              <span className="text-xs text-gray-500">
                {role.replace(/_/g, ' ')}
              </span>
            </div>
          </div>
        )}

        {/* Separador */}
        <div className="w-px h-5 bg-slate-200" />

        {/* Cerrar sesión */}
        <button
          onClick={logout}
          className="flex items-center gap-2 text-sm text-slate-500 hover:text-red-500 transition-colors px-3 py-1.5 rounded-lg hover:bg-red-50"
        >
          <LogOut size={14}/>
          Cerrar sesión
        </button>
      </div>
    </header>
  );
}