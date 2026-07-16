import { SolicitudPedido } from '../../models/SolicitudPedido';

export const aprobarFuenteUseCase = (solPedRepository) => ({
  async execute(id, { proveedorId, convenioId }) {
    if (!id) throw new Error('El id de la SolPed es requerido');
    if (!proveedorId) throw new Error('Debe seleccionar un proveedor homologado');
    if (!convenioId) throw new Error('RN-MM-001: debe seleccionar un convenio marco vigente');

    const raw = await solPedRepository.aprobarFuente(id, {
      proveedorId: Number(proveedorId),
      convenioId: Number(convenioId),
    });
    return SolicitudPedido.fromApi(raw);
  },
});
