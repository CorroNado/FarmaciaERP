import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.4 (cont.) - Corregir Lote según Observaciones del Comité
export const corregirLoteUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.corregir(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
