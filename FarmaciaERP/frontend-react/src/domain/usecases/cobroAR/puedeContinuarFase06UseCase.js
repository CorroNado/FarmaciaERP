export const puedeContinuarFase06UseCase = (cobroARRepository) => ({
  async execute(contabilizacionARId) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    return cobroARRepository.puedeContinuarFase06(contabilizacionARId);
  },
});
