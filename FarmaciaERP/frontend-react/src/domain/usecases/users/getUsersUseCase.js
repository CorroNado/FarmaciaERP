import { User } from '../../models/User';

export const getUsersUseCase = (userRepository) => ({
  async execute(filters = {}) {
    const raw = await userRepository.getAll(filters);
    return raw.map((item) => User.fromApi(item));
  },
});