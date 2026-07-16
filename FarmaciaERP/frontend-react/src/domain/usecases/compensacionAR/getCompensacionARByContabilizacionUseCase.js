import { CompensacionAR } from '../../models/CompensacionAR';

export const getCompensacionARByContabilizacionUseCase = (compensacionARRepository) => ({
  async execute(contabilizacionARId) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    const raw = await compensacionARRepository.getByContabilizacionAR(contabilizacionARId);
    return raw ? CompensacionAR.fromApi(raw) : null;
  },
});
