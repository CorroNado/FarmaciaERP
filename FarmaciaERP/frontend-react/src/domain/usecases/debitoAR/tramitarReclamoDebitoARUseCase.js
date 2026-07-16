import { DebitoAR } from '../../models/DebitoAR';

export const tramitarReclamoDebitoARUseCase = (debitoARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del débito es requerido');
    const raw = await debitoARRepository.tramitarReclamo(id);
    return DebitoAR.fromApi(raw);
  },
});
