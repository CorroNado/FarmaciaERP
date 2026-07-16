import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const conciliarLotesPOSUseCase = (contabilizacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la contabilización AR es requerido');
    const raw = await contabilizacionARRepository.conciliarLotesPOS(id);
    return ContabilizacionAR.fromApi(raw);
  },
});
