import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// Inicio del cierre de transacción — a partir de la disputa resuelta en Fase 02 (RN-AP3-01)
export const iniciarAjusteContableUseCase = (ajusteContableRepository) => ({
  async execute({ disputaComercialId }) {
    if (!disputaComercialId) {
      throw new Error('Debe seleccionarse la disputa comercial resuelta en el workflow del ERP');
    }

    const payload = AjusteContableRegularizacion.toApiIniciar({ disputaComercialId });
    const raw = await ajusteContableRepository.iniciar(payload);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
