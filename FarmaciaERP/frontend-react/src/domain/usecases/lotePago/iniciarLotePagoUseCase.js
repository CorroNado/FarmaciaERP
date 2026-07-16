import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// Inicio del armado del lote — a partir de los ajustes contables regularizados en Fase 03 (RN-AP4-01)
export const iniciarLotePagoUseCase = (lotePagoRepository) => ({
  async execute({ ajusteContableIds }) {
    if (!ajusteContableIds || ajusteContableIds.length === 0) {
      throw new Error('Debe seleccionarse al menos un ajuste contable regularizado en la Fase 03');
    }

    const payload = LotePagoTesoreria.toApiIniciar({ ajusteContableIds });
    const raw = await lotePagoRepository.iniciar(payload);
    return LotePagoTesoreria.fromApi(raw);
  },
});
