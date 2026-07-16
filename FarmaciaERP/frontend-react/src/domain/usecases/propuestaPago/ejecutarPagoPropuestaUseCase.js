import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.3 - Ejecutar Ejecución de Pago
export const ejecutarPagoPropuestaUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.ejecutarPago(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
