import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.1 - Introducir Parámetros de Pago en F110 (sociedad, vía de pago, fecha)
export const introducirParametrosPagoUseCase = (propuestaPagoRepository) => ({
  async execute(id, { sociedad, viaPago, fechaPago }) {
    const payload = PropuestaPagoAutomatica.toApiParametros({ sociedad, viaPago, fechaPago });
    const raw = await propuestaPagoRepository.introducirParametros(id, payload);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
