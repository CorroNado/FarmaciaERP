export const puedeContinuarFase04UseCase = (recetaMedicaARRepository) => ({
  async execute(contabilizacionARId) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    return recetaMedicaARRepository.puedeContinuarFase04(contabilizacionARId);
  },
});
