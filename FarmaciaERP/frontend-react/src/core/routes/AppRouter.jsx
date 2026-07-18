import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import PrivateRoute      from './PrivateRoute';
import MainLayout        from '@/presentation/components/layout/MainLayout';
import LoginPage         from '@/presentation/pages/LoginPage';
import DashboardPage     from '@/presentation/pages/DashboardPage';
import UserListPage      from '@/presentation/pages/Seguridad/UserListPage';
import UserEditPage      from '@/presentation/pages/Seguridad/UserEditPage';
import AccessReportPage  from '@/presentation/pages/Seguridad/AccessReportPage';
import ComingSoonPage    from '@/presentation/pages/ComingSoonPage';
import VentaPage         from '@/presentation/pages/Ventas/VentaPage';
import ProveedorConvenioPage from '@/presentation/pages/Logistica/ProveedorConvenioPage';
import SolPedPage        from '@/presentation/pages/Logistica/SolPedPage';
import OrdenCompraPage   from '@/presentation/pages/Logistica/OrdenCompraPage';
import MigoPage          from '@/presentation/pages/Logistica/MigoPage';
import InspeccionCalidadPage from '@/presentation/pages/Logistica/InspeccionCalidadPage';
import DistribucionPage  from '@/presentation/pages/Logistica/DistribucionPage';
import MiroPage          from '@/presentation/pages/Logistica/MiroPage';
import ConciliacionPage  from '@/presentation/pages/Logistica/ConciliacionPage';
import PagoPage          from '@/presentation/pages/Logistica/PagoPage';
import CierreCajaPage    from '@/presentation/pages/FI_AR/CierreCajaPage';
import ContabilizacionARPage from '@/presentation/pages/FI_AR/ContabilizacionARPage';
import RecetaMedicaARPage from '@/presentation/pages/FI_AR/RecetaMedicaARPage';
import DebitoARPage      from '@/presentation/pages/FI_AR/DebitoARPage';
import CobroARPage       from '@/presentation/pages/FI_AR/CobroARPage';
import CompensacionARPage from '@/presentation/pages/FI_AR/CompensacionARPage';
import ContratacionPage   from '@/presentation/pages/RRHH/ContratacionPage';
import AsistenciaPage     from '@/presentation/pages/RRHH/AsistenciaPage';
import PlanillaPage       from '@/presentation/pages/RRHH/PlanillaPage';
import { ContabilidadPage } from '@/presentation/pages/Contabilidad/ContabilidadPage';

function PrivateLayout({ children }) {
  return (
    <PrivateRoute>
      <MainLayout>
        {children}
      </MainLayout>
    </PrivateRoute>
  );
}

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Pública */}
        <Route path="/login" element={<LoginPage />} />

        {/* Privadas con layout compartido */}
        <Route path="/"              element={<PrivateLayout><DashboardPage /></PrivateLayout>} />
        <Route path="/usuarios"      element={<PrivateLayout><UserListPage /></PrivateLayout>} />
        <Route path="/users/edit/:id" element={<PrivateLayout><UserEditPage /></PrivateLayout>} />
        <Route path="/reports/access/:id" element={<PrivateLayout><AccessReportPage /></PrivateLayout>} />

        <Route path="/osd"            element={<PrivateLayout><VentaPage /></PrivateLayout>} />
        <Route path="/mm"              element={<PrivateLayout><ProveedorConvenioPage /></PrivateLayout>} />
        <Route path="/mm/proveedores"  element={<PrivateLayout><ProveedorConvenioPage /></PrivateLayout>} />
        <Route path="/mm/solped"       element={<PrivateLayout><SolPedPage /></PrivateLayout>} />
        <Route path="/mm/orden-compra" element={<PrivateLayout><OrdenCompraPage /></PrivateLayout>} />
        <Route path="/mm/migo"         element={<PrivateLayout><MigoPage /></PrivateLayout>} />
        <Route path="/mm/inspeccion-calidad" element={<PrivateLayout><InspeccionCalidadPage /></PrivateLayout>} />
        <Route path="/mm/distribucion" element={<PrivateLayout><DistribucionPage /></PrivateLayout>} />
        <Route path="/mm/miro" element={<PrivateLayout><MiroPage /></PrivateLayout>} />
        <Route path="/mm/conciliacion" element={<PrivateLayout><ConciliacionPage /></PrivateLayout>} />
        <Route path="/mm/pago" element={<PrivateLayout><PagoPage /></PrivateLayout>} />
        <Route path="/qm"           element={<PrivateLayout><ComingSoonPage title="QM – Calidad y Regulación Sanitaria" /></PrivateLayout>} />
        <Route path="/crm"         element={<PrivateLayout><ComingSoonPage title="CRM – Marketing y Fidelización" /></PrivateLayout>} />
        <Route path="/fico"                element={<PrivateLayout><CierreCajaPage /></PrivateLayout>} />
        <Route path="/fico/cierre-caja"    element={<PrivateLayout><CierreCajaPage /></PrivateLayout>} />
        <Route path="/fico/contabilizacion" element={<PrivateLayout><ContabilizacionARPage /></PrivateLayout>} />
        <Route path="/fico/recetas" element={<PrivateLayout><RecetaMedicaARPage /></PrivateLayout>} />
        <Route path="/fico/debitos" element={<PrivateLayout><DebitoARPage /></PrivateLayout>} />
        <Route path="/fico/cobros" element={<PrivateLayout><CobroARPage /></PrivateLayout>} />
        <Route path="/fico/compensacion-final" element={<PrivateLayout><CompensacionARPage /></PrivateLayout>} />
        <Route path="/rrhh"              element={<PrivateLayout><ContratacionPage /></PrivateLayout>} />
        <Route path="/rrhh/contratacion" element={<PrivateLayout><ContratacionPage /></PrivateLayout>} />
        <Route path="/rrhh/asistencia"   element={<PrivateLayout><AsistenciaPage /></PrivateLayout>} />
        <Route path="/rrhh/planilla"     element={<PrivateLayout><PlanillaPage /></PrivateLayout>} />
        <Route path="/fico/contabilidad"   element={<PrivateLayout><ContabilidadPage /></PrivateLayout>} />
        <Route path="/rrhh"              element={<PrivateLayout><ComingSoonPage title="SuccessFactors – Talento Humano" /></PrivateLayout>} />
        <Route path="/pmps"   element={<PrivateLayout><ComingSoonPage title="PM/PS – Infraestructura y Expansión" /></PrivateLayout>} />
        <Route path="/bi"         element={<PrivateLayout><ComingSoonPage title="Analytics Cloud – Inteligencia de Negocio" /></PrivateLayout>} />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
