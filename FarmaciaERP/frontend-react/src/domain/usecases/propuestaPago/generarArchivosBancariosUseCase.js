import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

// 5.3 (cont.) - Generar Archivos Bancarios Planos (IDoc / N43)
export const generarArchivosBancariosUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.generarArchivos(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
