import { tokenStorage } from '@/core/utils/tokenStorage';

export const loginUseCase = (authService) => ({
  async execute(credentials) {
    if (!credentials.email || !credentials.password) {
      throw new Error('Email y contraseña son requeridos');
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(credentials.email)) {
      throw new Error('El formato del email no es válido');
    }

    const { token } = await authService.login(credentials); // ← token no access_token

    if (!token) {
      throw new Error('La API no devolvió un token válido');
    }

    tokenStorage.set(token);
    return token;
  },
});