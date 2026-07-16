import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.7 - Conciliar Cuentas Puente Financieras y Compensar Cuenta Transitoria del Banco
// (Gerente de Finanzas / Tesorero). Concluye el ciclo FI-AP: obligación con el proveedor extinguida.
export const conciliarCuentasPuenteUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.conciliar(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
