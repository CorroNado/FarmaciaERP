export const editarAsistenciaUseCase = (asistenciaRepository) => ({
  async execute(id, datos, usuario) {
    return asistenciaRepository.editar(id, datos, usuario);
  },
});
