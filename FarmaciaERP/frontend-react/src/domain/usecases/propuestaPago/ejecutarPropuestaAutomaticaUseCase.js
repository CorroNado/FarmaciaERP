import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.1 (cont.) - Ejecutar Propuesta de Pago Automática (Sistema ERP)
export const ejecutarPropuestaAutomaticaUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.ejecutarPropuesta(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
