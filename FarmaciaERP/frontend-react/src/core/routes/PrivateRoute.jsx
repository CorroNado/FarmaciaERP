import { Navigate } from 'react-router-dom';
import { tokenStorage } from '@/core/utils/tokenStorage';

export default function PrivateRoute({ children }) {
  const isAuthenticated = tokenStorage.exists();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}