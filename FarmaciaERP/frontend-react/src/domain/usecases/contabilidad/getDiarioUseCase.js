export const getDiarioUseCase = (contabilidadRepository) => async (fechaInicio, fechaFin) => {
  if (!fechaInicio || !fechaFin) {
    throw new Error("El rango de fechas es requerido para consultar el Libro Diario");
  }
  return await contabilidadRepository.getLibroDiario(fechaInicio, fechaFin);
};