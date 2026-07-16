import { InspeccionCalidad } from '../../models/InspeccionCalidad';

export const rechazarLoteUseCase = (inspeccionCalidadRepository) => ({
  async execute({ entradaMercanciaId, motivoRechazo, muestreoConforme, registroSanitarioVigente, empaqueConforme }) {
    if (!entradaMercanciaId) {
      throw new Error('Debe seleccionar un lote en cuarentena (entrada MIGO)');
    }
    // RN-E5-007: el rechazo requiere motivo documentado
    if (!motivoRechazo || !motivoRechazo.trim()) {
      throw new Error('RN-E5-007: debe indicar el motivo del rechazo');
    }

    const payload = InspeccionCalidad.toApiRechazar({
      entradaMercanciaId, motivoRechazo, muestreoConforme, registroSanitarioVigente, empaqueConforme,
    });
    const raw = await inspeccionCalidadRepository.rechazar(payload);
    return InspeccionCalidad.fromApi(raw);
  },
});
