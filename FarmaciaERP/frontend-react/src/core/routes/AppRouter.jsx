import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import PrivateRoute      from './PrivateRoute';
import MainLayout        from '@/presentation/components/layout/MainLayout';
import LoginPage         from '@/presentation/pages/LoginPage';
import DashboardPage     from '@/presentation/pages/DashboardPage';
import UserListPage      from '@/presentation/pages/users/UserListPage';
import UserEditPage      from '@/presentation/pages/users/UserEditPage';
import AccessReportPage  from '@/presentation/pages/reports/AccessReportPage';
import ComingSoonPage    from '@/presentation/pages/ComingSoonPage';
import VentasPage           from '@/presentation/pages/ventas/VentasPage';
import CheckoutPage         from '@/presentation/pages/ventas/CheckoutPage';
import BusinessPartnersPage from '@/presentation/pages/ventas/BusinessPartnersPage';
import PricingEnginePage    from '@/presentation/pages/ventas/PricingEnginePage';
import FinancialFiscalPage  from '@/presentation/pages/ventas/FinancialFiscalPage';
import LogisticsHubPage     from '@/presentation/pages/ventas/LogisticsHubPage';
import ReverseOpsPage       from '@/presentation/pages/ventas/ReverseOpsPage';

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

        <Route path="/osd"             element={<PrivateLayout><VentasPage /></PrivateLayout>} />
        <Route path="/osd/checkout"    element={<PrivateLayout><CheckoutPage /></PrivateLayout>} />
        <Route path="/osd/partners"    element={<PrivateLayout><BusinessPartnersPage /></PrivateLayout>} />
        <Route path="/osd/pricing"     element={<PrivateLayout><PricingEnginePage /></PrivateLayout>} />
        <Route path="/osd/financial"   element={<PrivateLayout><FinancialFiscalPage /></PrivateLayout>} />
        <Route path="/osd/logistics"   element={<PrivateLayout><LogisticsHubPage /></PrivateLayout>} />
        <Route path="/osd/reverseops"  element={<PrivateLayout><ReverseOpsPage /></PrivateLayout>} />

        <Route path="/mm"           element={<PrivateLayout><ComingSoonPage title="MM – Compras e Inventarios" /></PrivateLayout>} />
        <Route path="/qm"           element={<PrivateLayout><ComingSoonPage title="QM – Calidad y Regulación Sanitaria" /></PrivateLayout>} />
        <Route path="/crm"         element={<PrivateLayout><ComingSoonPage title="CRM – Marketing y Fidelización" /></PrivateLayout>} />
        <Route path="/fico"          element={<PrivateLayout><ComingSoonPage title="FI/CO – Finanzas y Control de Gestión" /></PrivateLayout>} />
        <Route path="/rrhh"              element={<PrivateLayout><ComingSoonPage title="SuccessFactors – Talento Humano" /></PrivateLayout>} />
        <Route path="/pmps"   element={<PrivateLayout><ComingSoonPage title="PM/PS – Infraestructura y Expansión" /></PrivateLayout>} />
        <Route path="/bi"         element={<PrivateLayout><ComingSoonPage title="Analytics Cloud – Inteligencia de Negocio" /></PrivateLayout>} />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}