import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.2 (cont.) - Corregir Errores y Reenviar Lote (Analista de Cuentas por Pagar)
export const corregirErroresYReenviarLoteUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.corregirReenviar(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
