import { CobroAR } from '../../models/CobroAR';

export const conciliarComisionesRetencionesUseCase = (cobroARRepository) => ({
  async execute(id, { comisionPct }) {
    if (!id) throw new Error('El id del cobro es requerido');
    const payload = CobroAR.toApiConciliar({ comisionPct });
    const raw = await cobroARRepository.conciliarComisiones(id, payload);
    return CobroAR.fromApi(raw);
  },
});
