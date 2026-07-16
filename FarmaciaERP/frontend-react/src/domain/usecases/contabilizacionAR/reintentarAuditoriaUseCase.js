import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const reintentarAuditoriaUseCase = (contabilizacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la contabilización AR es requerido');
    const raw = await contabilizacionARRepository.reintentarAuditoria(id);
    return ContabilizacionAR.fromApi(raw);
  },
});
