import { Planilla } from '../../models/Planilla';

export const getPlanillaByIdUseCase = (planillaRepository) => ({
  async execute(id) {
    const raw = await planillaRepository.getById(id);
    return Planilla.fromApi(raw);
  },
});
