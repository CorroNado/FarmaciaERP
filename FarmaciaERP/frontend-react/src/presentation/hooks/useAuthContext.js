import { useContext }  from 'react';
import { AuthContext } from '../context/AuthContext'; // ← apunta al nuevo archivo

export function useAuthContext() {
  return useContext(AuthContext);
}