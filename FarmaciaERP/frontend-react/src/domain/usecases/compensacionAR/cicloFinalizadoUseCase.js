export const cicloFinalizadoUseCase = (compensacionARRepository) => ({
  async execute(contabilizacionARId) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    return compensacionARRepository.cicloFinalizado(contabilizacionARId);
  },
});
