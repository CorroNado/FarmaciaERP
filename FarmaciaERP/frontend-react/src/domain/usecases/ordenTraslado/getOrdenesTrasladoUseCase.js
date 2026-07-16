import { OrdenTraslado } from '../../models/OrdenTraslado';

export const getOrdenesTrasladoUseCase = (ordenTrasladoRepository) => ({
  async execute(filters = {}) {
    const raw = await ordenTrasladoRepository.getAll(filters);
    return raw.map((item) => OrdenTraslado.fromApi(item));
  },
});
