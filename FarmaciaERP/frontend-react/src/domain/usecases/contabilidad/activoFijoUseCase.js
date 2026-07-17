export const getActivosFijosUseCase = (contabilidadRepository) => async () => {
  return await contabilidadRepository.getActivosFijos();
};

export const crearActivoFijoUseCase = (contabilidadRepository) => async (activoData) => {
  if (activoData.costoAdquisicion <= 0) {
    throw new Error("El costo de adquisicion debe ser mayor a cero");
  }
  return await contabilidadRepository.crearActivoFijo(activoData);
};