import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.4 - Verificar Fondos y Validar Lote (Gerente de Tesorería)
export const verificarFondosYValidarLoteUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.verificarFondos(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
