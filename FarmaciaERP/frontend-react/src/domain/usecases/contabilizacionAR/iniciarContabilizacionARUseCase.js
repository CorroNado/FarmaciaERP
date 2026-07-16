import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const iniciarContabilizacionARUseCase = (contabilizacionARRepository) => ({
  async execute({ cierreCajaId }) {
    if (!cierreCajaId) {
      throw new Error('Debe seleccionar un cierre de caja clasificado (Fase 01) para iniciar la Fase 02');
    }

    const payload = ContabilizacionAR.toApiIniciar({ cierreCajaId });
    const raw = await contabilizacionARRepository.iniciar(payload);
    return ContabilizacionAR.fromApi(raw);
  },
});
