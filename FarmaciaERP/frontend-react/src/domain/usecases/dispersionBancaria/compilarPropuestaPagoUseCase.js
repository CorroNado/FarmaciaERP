import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.1 - Compilar Propuesta de Pago (F110) recibida desde la Fase 05 (Sistema ERP)
export const compilarPropuestaPagoUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.compilar(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
