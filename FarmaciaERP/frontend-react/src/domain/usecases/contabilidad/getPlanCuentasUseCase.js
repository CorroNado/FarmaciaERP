export const getPlanCuentasUseCase = (contabilidadRepository) => async () => {
  return await contabilidadRepository.getPlanCuentas();
};

export const crearCuentaContableUseCase = (contabilidadRepository) => async (cuentaData) => {
  return await contabilidadRepository.crearCuentaContable(cuentaData);
};