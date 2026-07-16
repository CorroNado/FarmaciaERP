import { DebitoAR } from '../../models/DebitoAR';

export const getDebitoARByIdUseCase = (debitoARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del débito es requerido');
    const raw = await debitoARRepository.getById(id);
    return DebitoAR.fromApi(raw);
  },
});
