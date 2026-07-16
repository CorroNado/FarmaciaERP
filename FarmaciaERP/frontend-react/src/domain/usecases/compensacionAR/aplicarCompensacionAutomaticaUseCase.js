import { CompensacionAR } from '../../models/CompensacionAR';

export const aplicarCompensacionAutomaticaUseCase = (compensacionARRepository) => ({
  async execute({ contabilizacionARId }) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    const payload = CompensacionAR.toApiAplicar({ contabilizacionARId });
    const raw = await compensacionARRepository.aplicar(payload);
    return CompensacionAR.fromApi(raw);
  },
});
