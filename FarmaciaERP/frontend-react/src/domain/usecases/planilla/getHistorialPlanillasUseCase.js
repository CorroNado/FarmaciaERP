import { PlanillaResumen } from '../../models/Planilla';

export const getHistorialPlanillasUseCase = (planillaRepository) => ({
  async execute() {
    const raw = await planillaRepository.historial();
    return raw.map((item) => PlanillaResumen.fromApi(item));
  },
});
