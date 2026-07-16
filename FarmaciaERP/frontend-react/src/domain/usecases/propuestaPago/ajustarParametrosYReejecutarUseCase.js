import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.2 (cont.) - Ajustar Parámetros y Reejecutar Propuesta
export const ajustarParametrosYReejecutarUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.ajustarReejecutar(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
