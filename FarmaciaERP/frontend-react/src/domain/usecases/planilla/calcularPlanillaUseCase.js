import { Planilla } from '../../models/Planilla';

export const calcularPlanillaUseCase = (planillaRepository) => ({
  async execute(mes, anio) {
    const raw = await planillaRepository.calcular({ mes, anio });
    return Planilla.fromApi(raw);
  },
});
