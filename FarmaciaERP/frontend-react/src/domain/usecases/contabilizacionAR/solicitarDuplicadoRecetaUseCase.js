import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const solicitarDuplicadoRecetaUseCase = (contabilizacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la contabilización AR es requerido');
    const raw = await contabilizacionARRepository.solicitarDuplicadoReceta(id);
    return ContabilizacionAR.fromApi(raw);
  },
});
