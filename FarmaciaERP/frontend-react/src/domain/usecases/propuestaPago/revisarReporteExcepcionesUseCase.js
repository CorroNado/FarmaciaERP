import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.2 - Revisar Reporte de Excepciones y Bloqueos
export const revisarReporteExcepcionesUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.revisarExcepciones(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
