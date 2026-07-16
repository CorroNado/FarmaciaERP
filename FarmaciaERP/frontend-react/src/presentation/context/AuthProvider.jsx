import { useState, useEffect } from 'react';
import { AuthContext }         from './AuthContext';
import { tokenStorage }        from '@/core/utils/tokenStorage';
import { useCases }            from '@/infrastructure';
import apiClient               from '@/core/api/apiClient';
import { ENDPOINTS }           from '@/core/api/endpoints';

function getEmailFromToken() {
  const payload = tokenStorage.getPayload();
  if (!payload) return null;
  return payload.email ?? payload.sub ?? null;
}

export function AuthProvider({ children }) {
  const [token,   setToken]   = useState(tokenStorage.get());
  const [user,    setUser]    = useState(null);
  const [error,   setError]   = useState(null);
  const [loading, setLoading] = useState(false);

  const isAuthenticated = !!token;

  useEffect(() => {
    let cancelled = false;

    async function loadUser() {
      if (!token) {
        if (!cancelled) setUser(null);
        return;
      }

      try {
        const email = getEmailFromToken();
        if (!email) return;

        const { data } = await apiClient.get(ENDPOINTS.users.list);
        const found = data.find((u) =>
          (u.email?.email ?? u.email) === email
        );

        if (!cancelled) {
  setUser(found ? {
    id:       found.id,
    email:    found.email?.email ?? found.email,
    nombre:   found.nombres?.Nombres   ?? '',
    apellido: found.nombres?.Apellidos ?? '',
    rol:      found.role ?? '',              // ← agregar esto
  } : { email: getEmailFromToken(), nombre: '', apellido: '', rol: '' });
}
      } catch {
        if (!cancelled) setUser({ email: getEmailFromToken(), nombre: '', apellido: '' });
      }
    }

    loadUser();
    return () => { cancelled = true; };
  }, [token]);

  async function login(credentials) {
    try {
      setLoading(true);
      setError(null);
      const newToken = await useCases.auth.login.execute(credentials);
      setToken(newToken);
      return true;
    } catch (err) {
      setError(err.message ?? 'Error al iniciar sesión');
      return false;
    } finally {
      setLoading(false);
    }
  }

  function logout() {
    tokenStorage.remove();
    setToken(null);
  }

  return (
    <AuthContext.Provider value={{
      token,
      user,
      isAuthenticated,
      loading,
      error,
      login,
      logout,
    }}>
      {children}
    </AuthContext.Provider>
  );
}