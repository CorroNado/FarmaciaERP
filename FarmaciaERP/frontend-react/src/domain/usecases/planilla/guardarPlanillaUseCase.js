import { Planilla } from '../../models/Planilla';

export const guardarPlanillaUseCase = (planillaRepository) => ({
  async execute(formData) {
    const payload = Planilla.toApiGuardar(formData);
    const raw = await planillaRepository.guardar(payload);
    return Planilla.fromApi(raw);
  },
});
