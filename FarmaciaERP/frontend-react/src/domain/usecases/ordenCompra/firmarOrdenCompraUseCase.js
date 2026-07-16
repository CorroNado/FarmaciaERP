import { OrdenCompra } from '../../models/OrdenCompra';

export const firmarOrdenCompraUseCase = (ordenCompraRepository) => ({
  async execute(id, fechaEntregaLimite) {
    if (!id) throw new Error('El id de la Orden de Compra es requerido');
    if (!fechaEntregaLimite || !fechaEntregaLimite.trim()) {
      throw new Error('RN-OC-003: la fecha límite de entrega es obligatoria');
    }

    const raw = await ordenCompraRepository.firmar(id, fechaEntregaLimite);
    return OrdenCompra.fromApi(raw);
  },
});
