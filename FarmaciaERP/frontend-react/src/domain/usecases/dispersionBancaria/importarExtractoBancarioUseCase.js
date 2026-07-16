import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.6 - Importar Extracto Bancario Digital del Día (FF.5) (Gerente de Finanzas / Tesorero)
export const importarExtractoBancarioUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.importarExtracto(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
