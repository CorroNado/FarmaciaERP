import { DisputaComercial } from '../../models/DisputaComercial';

// Notificación de discrepancia recibida — abre la disputa desde la excepción de Fase 01 (RN-AP2-01)
export const abrirDisputaComercialUseCase = (disputaComercialRepository) => ({
  async execute({ excepcionFacturacionId }) {
    if (!excepcionFacturacionId) {
      throw new Error('Debe seleccionarse la excepción de facturación notificada a Compras');
    }

    const payload = DisputaComercial.toApiAbrir({ excepcionFacturacionId });
    const raw = await disputaComercialRepository.abrir(payload);
    return DisputaComercial.fromApi(raw);
  },
});
