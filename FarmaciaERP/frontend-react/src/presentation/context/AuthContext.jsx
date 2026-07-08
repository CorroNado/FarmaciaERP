import { createContext } from 'react';

export const AuthContext = createContext({
  token:           null,
  user:            null,
  isAuthenticated: false,
  loading:         false,
  error:           null,
  login:           async () => false,
  logout:          () => {},
});