import { Cotizacion } from '../../models/Cotizacion';

export const crearCotizacionUseCase = (cotizacionRepository) => ({
  async execute({ clienteId, vigenciaDias, items }) {
    if (!clienteId) {
      throw new Error('Selecciona un cliente para continuar');
    }
    if (!items || items.length === 0) {
      throw new Error('Agrega al menos un producto a la cotización');
    }
    if (!vigenciaDias) {
      throw new Error('Selecciona la vigencia de la oferta');
    }

    const payload = Cotizacion.toApiCrear({ clienteId, vigenciaDias, items });
    const raw = await cotizacionRepository.create(payload);
    return Cotizacion.fromApi(raw);
  },
});
