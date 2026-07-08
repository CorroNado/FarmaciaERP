import { User } from '../../models/User';

export const createUserUseCase = (userRepository) => ({
  async execute(formData) {
    if (!formData.nombre || !formData.apellido) {
      throw new Error('Nombre y apellido son requeridos');
    }

    if (!formData.email) {
      throw new Error('El email es requerido');
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      throw new Error('El formato del email no es válido');
    }

    if (!formData.password || formData.password.length < 6) {
      throw new Error('La contraseña debe tener al menos 6 caracteres');
    }

    const payload = User.toApi(formData);
    const raw     = await userRepository.create(payload);
    return User.fromApi(raw);
  },
});