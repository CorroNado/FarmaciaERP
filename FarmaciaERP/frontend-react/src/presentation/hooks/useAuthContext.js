import { useContext }  from 'react';
import { AuthContext } from '../context/authContext'; // ← apunta al nuevo archivo

export function useAuthContext() {
  return useContext(AuthContext);
}