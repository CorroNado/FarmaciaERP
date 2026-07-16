import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.2 (cont.) - Aprobar Propuesta de Pago Final
export const aprobarPropuestaFinalUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.aprobar(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
