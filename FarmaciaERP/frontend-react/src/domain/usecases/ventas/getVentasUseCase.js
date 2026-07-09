import { Venta } from '../../models/Venta';

export const getVentasUseCase = (ventaRepository) => ({
  async execute(filters = {}) {
    const raw = await ventaRepository.getAll(filters);
    return raw.map((v) => Venta.fromApi(v));
  },
});