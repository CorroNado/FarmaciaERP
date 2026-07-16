import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// Laboratorio / Droguería · paso 3.1.a (cont.): Evaluar y Enviar Nota de Crédito.
export const evaluarYEnviarNotaCreditoUseCase = (ajusteContableRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del ajuste contable es requerido');
    const raw = await ajusteContableRepository.evaluarEnvioNotaCredito(id);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
