import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// FI-AP · paso 3.2: Ejecutar Asiento de Regularización por diferencias permitidas.
export const ejecutarAsientoRegularizacionUseCase = (ajusteContableRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del ajuste contable es requerido');
    const raw = await ajusteContableRepository.asientoRegularizacion(id);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
