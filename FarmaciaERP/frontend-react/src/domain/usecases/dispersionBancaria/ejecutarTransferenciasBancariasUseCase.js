import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.5 - Ejecutar Transferencias en Banca Empresa (Token) (Analista de Cuentas por Pagar)
export const ejecutarTransferenciasBancariasUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.transferir(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
