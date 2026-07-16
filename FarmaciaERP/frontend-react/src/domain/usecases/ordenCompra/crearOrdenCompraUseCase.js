import { OrdenCompra } from '../../models/OrdenCompra';

export const crearOrdenCompraUseCase = (ordenCompraRepository) => ({
  async execute({ solPedId, centroDestino }) {
    if (!solPedId) {
      throw new Error('Debe seleccionar una SolPed con fuente de aprovisionamiento aprobada');
    }
    if (!centroDestino || !centroDestino.trim()) {
      throw new Error('RN-OC-003: el centro de destino (muelle) es obligatorio');
    }

    const payload = OrdenCompra.toApiCreate({ solPedId, centroDestino });
    const raw = await ordenCompraRepository.create(payload);
    return OrdenCompra.fromApi(raw);
  },
});
