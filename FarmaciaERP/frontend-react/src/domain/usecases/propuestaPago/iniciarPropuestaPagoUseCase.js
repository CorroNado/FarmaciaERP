import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// Inicio de la propuesta - a partir del lote de pagos conciliado en Fase 04 (RN-AP5-01)
export const iniciarPropuestaPagoUseCase = (propuestaPagoRepository) => ({
  async execute({ lotePagoTesoreriaId }) {
    if (!lotePagoTesoreriaId) {
      throw new Error('Debe seleccionarse un lote de pagos conciliado en la Fase 04');
    }

    const payload = PropuestaPagoAutomatica.toApiIniciar({ lotePagoTesoreriaId });
    const raw = await propuestaPagoRepository.iniciar(payload);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
