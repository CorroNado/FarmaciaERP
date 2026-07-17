export const getEstadoResultadosUseCase = (contabilidadRepository) => async (periodoId) => {
  return await contabilidadRepository.getEstadoResultados(periodoId);
};