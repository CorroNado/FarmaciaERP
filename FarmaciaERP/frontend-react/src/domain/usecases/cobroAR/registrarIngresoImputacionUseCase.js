import { CobroAR } from '../../models/CobroAR';

export const registrarIngresoImputacionUseCase = (cobroARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del cobro es requerido');
    const raw = await cobroARRepository.registrarIngreso(id);
    return CobroAR.fromApi(raw);
  },
});
