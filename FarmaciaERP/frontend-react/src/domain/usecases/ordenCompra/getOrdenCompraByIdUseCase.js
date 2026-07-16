import { OrdenCompra } from '../../models/OrdenCompra';

export const getOrdenCompraByIdUseCase = (ordenCompraRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la Orden de Compra es requerido');
    const raw = await ordenCompraRepository.getById(id);
    return OrdenCompra.fromApi(raw);
  },
});
