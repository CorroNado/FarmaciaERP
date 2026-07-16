import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.2 - Negociar Descuento por Pronto Pago (Gerente de Tesorería)
export const negociarDescuentoProntoPagoUseCase = (lotePagoRepository) => ({
  async execute(id, { descuentoPct }) {
    const payload = LotePagoTesoreria.toApiNegociarDescuento({ descuentoPct });
    const raw = await lotePagoRepository.negociarDescuento(id, payload);
    return LotePagoTesoreria.fromApi(raw);
  },
});
