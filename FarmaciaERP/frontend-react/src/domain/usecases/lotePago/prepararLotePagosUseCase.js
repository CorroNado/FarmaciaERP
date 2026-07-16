import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.3 - Preparar Lote de Pagos (F110 SAP)
export const prepararLotePagosUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.preparar(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
