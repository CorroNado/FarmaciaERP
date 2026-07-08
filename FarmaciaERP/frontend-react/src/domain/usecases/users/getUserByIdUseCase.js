import { User } from '../../models/User';

export const getUserByIdUseCase = (userRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del usuario es requerido');

    const raw = await userRepository.getById(id);
    return User.fromApi(raw);
  },
});