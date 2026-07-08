import { useNavigate }    from 'react-router-dom';
import { useAuthContext } from './useAuthContext';

export function useAuth() {
  const navigate = useNavigate();
  const auth     = useAuthContext();

  async function handleLogin(credentials) {
    const success = await auth.login(credentials);
    if (success) navigate('/');
  }

  function handleLogout() {
    auth.logout();
    navigate('/login');
  }

  return {
    token:           auth.token,
    user:            auth.user,
    isAuthenticated: auth.isAuthenticated,
    loading:         auth.loading,
    error:           auth.error,
    login:           handleLogin,
    logout:          handleLogout,
  };
}