import { OrdenCompra } from '../../models/OrdenCompra';

export const getOrdenesCompraUseCase = (ordenCompraRepository) => ({
  async execute(filters = {}) {
    const raw = await ordenCompraRepository.getAll(filters);
    return raw.map((item) => OrdenCompra.fromApi(item));
  },
});
