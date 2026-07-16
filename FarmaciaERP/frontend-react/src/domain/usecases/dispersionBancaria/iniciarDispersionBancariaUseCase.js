import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// Inicio de la dispersión - a partir de la propuesta de pago concluida en Fase 05 (RN-AP6-01)
export const iniciarDispersionBancariaUseCase = (dispersionBancariaRepository) => ({
  async execute({ propuestaPagoAutomaticaId }) {
    if (!propuestaPagoAutomaticaId) {
      throw new Error('Debe seleccionarse una propuesta de pago concluida en la Fase 05');
    }

    const payload = DispersionBancariaCierre.toApiIniciar({ propuestaPagoAutomaticaId });
    const raw = await dispersionBancariaRepository.iniciar(payload);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
