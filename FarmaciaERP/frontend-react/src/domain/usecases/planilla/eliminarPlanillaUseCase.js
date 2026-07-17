export const eliminarPlanillaUseCase = (planillaRepository) => ({
  async execute(id) {
    return planillaRepository.eliminar(id);
  },
});
