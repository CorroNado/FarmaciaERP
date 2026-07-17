export const marcarSalidaUseCase = (asistenciaRepository) => ({
  async execute(id, usuario) {
    return asistenciaRepository.marcarSalida(id, usuario);
  },
});
