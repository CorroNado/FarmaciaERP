import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.4 (cont.) - ¿Lote Aprobado? Someter al Comité Semanal de Tesorería
export const someterLoteAComiteUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.someterComite(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
