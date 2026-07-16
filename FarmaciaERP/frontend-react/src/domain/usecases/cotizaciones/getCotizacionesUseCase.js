import { Cotizacion } from '../../models/Cotizacion';

export const getCotizacionesUseCase = (cotizacionRepository) => ({
  async execute(filters = {}) {
    const raw = await cotizacionRepository.getAll(filters);
    return raw.map((item) => Cotizacion.fromApi(item));
  },
});
