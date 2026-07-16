import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

export const getDispersionBancariaByIdUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.getById(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
