import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// FI-AP · paso 3.1.b: Registrar Nota de Crédito en SAP (recibida directamente).
export const registrarNotaCreditoUseCase = (ajusteContableRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del ajuste contable es requerido');
    const raw = await ajusteContableRepository.registrarNotaCredito(id);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
