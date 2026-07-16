import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.4 - Aplicar Firma Digital con Token Bancario (Analista de Cuentas por Pagar)
export const aplicarFirmaDigitalUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.firmar(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
