import { CobroAR } from '../../models/CobroAR';

export const interpretarArchivoTransferenciaUseCase = (cobroARRepository) => ({
  async execute({ contabilizacionARId, retenciones }) {
    if (!contabilizacionARId) throw new Error('El id de la contabilización AR es requerido');
    if (retenciones === undefined || retenciones === null || retenciones === '') {
      throw new Error('Las retenciones aplicadas son requeridas');
    }
    const payload = CobroAR.toApiInterpretar({ contabilizacionARId, retenciones });
    const raw = await cobroARRepository.interpretar(payload);
    return CobroAR.fromApi(raw);
  },
});
