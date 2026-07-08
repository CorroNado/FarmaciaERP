import { User } from '../../models/User';

export const editUserUseCase = (userRepository) => ({
  async execute(id, formData) {
    if (!id)             throw new Error('El id del usuario es requerido');
    if (!formData.nombre)   throw new Error('Nombre es requerido');
    if (!formData.apellido) throw new Error('Apellido es requerido');
    if (!formData.email)    throw new Error('Email es requerido');

    const payload = User.toApiUpdate(formData); // ← usa el nuevo método
    const raw     = await userRepository.update(id, payload);
    return User.fromApi(raw);
  },
});