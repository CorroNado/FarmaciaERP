export const getAjusteTotalUseCase = (debitoARRepository) => ({
  async execute(contabilizacionARId) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    return debitoARRepository.ajusteTotal(contabilizacionARId);
  },
});
