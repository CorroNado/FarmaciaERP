export const deleteUserUseCase = (userRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del usuario es requerido');

    await userRepository.delete(id);
    return id;
  },
});