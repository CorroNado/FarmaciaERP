export const deleteEmpleadoUseCase = (empleadoRepository) => ({
  async execute(id, usuario) {
    if (!id) throw new Error('El id del colaborador es requerido');
    await empleadoRepository.delete(id, usuario);
    return id;
  },
});
