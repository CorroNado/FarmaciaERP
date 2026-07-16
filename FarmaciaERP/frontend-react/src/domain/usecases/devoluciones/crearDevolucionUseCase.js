import { Devolucion } from '../../models/Devolucion';

export const crearDevolucionUseCase = (devolucionRepository) => ({
  async execute({ ventaId, motivo, accion, items }) {
    if (!ventaId) {
      throw new Error('Selecciona la venta de origen');
    }
    if (!items || items.length === 0) {
      throw new Error('Selecciona al menos un ítem y cantidad a devolver');
    }
    if (!motivo) {
      throw new Error('Selecciona el motivo de la devolución');
    }
    if (!accion) {
      throw new Error('Selecciona la acción a aplicar');
    }

    const payload = Devolucion.toApiCrear({ ventaId, motivo, accion, items });
    const raw = await devolucionRepository.create(payload);
    return Devolucion.fromApi(raw);
  },
});
