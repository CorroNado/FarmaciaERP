import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.2 - Validar Propuesta de Duplicados / Bloqueos (Comprador / Category Manager)
export const validarPropuestaDuplicadosUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.validar(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
