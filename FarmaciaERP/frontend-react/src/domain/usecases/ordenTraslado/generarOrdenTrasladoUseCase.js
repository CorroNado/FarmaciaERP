import { OrdenTraslado } from '../../models/OrdenTraslado';

export const generarOrdenTrasladoUseCase = (ordenTrasladoRepository) => ({
  async execute({ inspeccionCalidadId, sucursalDestinoId, guiaRemision }) {
    // RN-E6-002: solo procede sobre un lote con Decisión de Empleo aprobada
    if (!inspeccionCalidadId) {
      throw new Error('RN-E6-002: debe seleccionar un lote con Decisión de Empleo aprobada');
    }
    if (!sucursalDestinoId) {
      throw new Error('Debe seleccionar la sucursal destino');
    }
    if (!guiaRemision || !guiaRemision.trim()) {
      throw new Error('La guía de remisión es obligatoria');
    }

    const payload = OrdenTraslado.toApiGenerar({ inspeccionCalidadId, sucursalDestinoId, guiaRemision });
    const raw = await ordenTrasladoRepository.generar(payload);
    return OrdenTraslado.fromApi(raw);
  },
});
