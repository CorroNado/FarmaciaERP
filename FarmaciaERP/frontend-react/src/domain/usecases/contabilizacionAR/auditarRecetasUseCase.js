import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const auditarRecetasUseCase = (contabilizacionARRepository) => ({
  async execute(id, { conforme, motivoObservacion }) {
    if (!id) throw new Error('El id de la contabilización AR es requerido');
    if (!conforme && !motivoObservacion?.trim()) {
      throw new Error('Debes registrar el motivo de la observación cuando la receta no es conforme');
    }

    const payload = ContabilizacionAR.toApiAuditar({ conforme, motivoObservacion });
    const raw = await contabilizacionARRepository.auditarRecetas(id, payload);
    return ContabilizacionAR.fromApi(raw);
  },
});
