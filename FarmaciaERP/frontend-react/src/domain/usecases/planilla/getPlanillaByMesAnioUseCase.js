import { Planilla } from '../../models/Planilla';

export const getPlanillaByMesAnioUseCase = (planillaRepository) => ({
  async execute(mes, anio) {
    const raw = await planillaRepository.getByMesAnio(mes, anio);
    return raw ? Planilla.fromApi(raw) : null;
  },
});
