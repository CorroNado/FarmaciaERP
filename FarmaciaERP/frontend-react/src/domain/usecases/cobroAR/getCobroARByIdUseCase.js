import { CobroAR } from '../../models/CobroAR';

export const getCobroARByIdUseCase = (cobroARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del cobro es requerido');
    const raw = await cobroARRepository.getById(id);
    return CobroAR.fromApi(raw);
  },
});
