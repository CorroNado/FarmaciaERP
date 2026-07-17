export const marcarEntradaUseCase = (asistenciaRepository) => ({
  async execute(id, usuario) {
    return asistenciaRepository.marcarEntrada(id, usuario);
  },
});
