import { CobroAR } from '../../models/CobroAR';

export const getCobroARByContabilizacionUseCase = (cobroARRepository) => ({
  async execute(contabilizacionARId) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    const raw = await cobroARRepository.getByContabilizacionAR(contabilizacionARId);
    return raw ? CobroAR.fromApi(raw) : null;
  },
});
