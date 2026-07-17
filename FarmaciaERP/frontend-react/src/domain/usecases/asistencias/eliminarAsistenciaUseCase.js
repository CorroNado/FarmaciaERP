export const eliminarAsistenciaUseCase = (asistenciaRepository) => ({
  async execute(id, datos, usuario) {
    return asistenciaRepository.eliminar(id, datos, usuario);
  },
});
