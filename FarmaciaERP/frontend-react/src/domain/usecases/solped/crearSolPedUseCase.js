import { SolicitudPedido } from '../../models/SolicitudPedido';

export const crearSolPedUseCase = (solPedRepository) => ({
  async execute(formData) {
    if (!formData.responsable) {
      throw new Error('El responsable de la SolPed es obligatorio');
    }
    if (!formData.centroCosto) {
      throw new Error('El centro de costos es obligatorio');
    }
    if (!formData.presupuesto || Number(formData.presupuesto) <= 0) {
      throw new Error('El presupuesto disponible debe ser mayor a 0');
    }
    if (!formData.detalles || formData.detalles.length === 0) {
      throw new Error('Calcula la sugerencia MRP y confirma al menos una línea de detalle');
    }
    for (const d of formData.detalles) {
      if (!d.cantidadSugerida || Number(d.cantidadSugerida) <= 0) {
        throw new Error('RN-E1-006: la cantidad a solicitar debe ser mayor a 0 en todas las líneas');
      }
    }

    const payload = SolicitudPedido.toApiCreate(formData);
    const raw = await solPedRepository.create(payload);
    return SolicitudPedido.fromApi(raw);
  },
});
