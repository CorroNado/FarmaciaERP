export const getBalanceGeneralUseCase = (contabilidadRepository) => async (periodoId) => {
  return await contabilidadRepository.getBalanceGeneral(periodoId);
};