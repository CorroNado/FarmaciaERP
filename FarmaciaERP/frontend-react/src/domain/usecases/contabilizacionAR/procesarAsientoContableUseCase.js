import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const procesarAsientoContableUseCase = (contabilizacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la contabilización AR es requerido');
    const raw = await contabilizacionARRepository.procesarAsiento(id);
    return ContabilizacionAR.fromApi(raw);
  },
});
