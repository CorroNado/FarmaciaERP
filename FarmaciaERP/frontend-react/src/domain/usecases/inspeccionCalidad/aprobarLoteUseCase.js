import { InspeccionCalidad } from '../../models/InspeccionCalidad';

export const aprobarLoteUseCase = (inspeccionCalidadRepository) => ({
  async execute({ entradaMercanciaId, muestreoConforme, registroSanitarioVigente, empaqueConforme }) {
    if (!entradaMercanciaId) {
      throw new Error('Debe seleccionar un lote en cuarentena (entrada MIGO)');
    }
    // RN-E5-006: los tres controles manuales deben estar conformes antes de aprobar
    if (!muestreoConforme || !registroSanitarioVigente || !empaqueConforme) {
      throw new Error('RN-E5-006: todos los controles deben estar conformes para aprobar el lote');
    }

    const payload = InspeccionCalidad.toApiAprobar({
      entradaMercanciaId, muestreoConforme, registroSanitarioVigente, empaqueConforme,
    });
    const raw = await inspeccionCalidadRepository.aprobar(payload);
    return InspeccionCalidad.fromApi(raw);
  },
});
