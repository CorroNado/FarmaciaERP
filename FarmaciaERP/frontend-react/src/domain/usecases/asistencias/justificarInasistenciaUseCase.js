export const justificarInasistenciaUseCase = (asistenciaRepository) => ({
  async execute(id, datos, usuario) {
    return asistenciaRepository.justificar(id, datos, usuario);
  },
});
