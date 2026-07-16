import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

export const getDispersionesBancariasUseCase = (dispersionBancariaRepository) => ({
  async execute() {
    const raw = await dispersionBancariaRepository.getAll();
    return raw.map((item) => DispersionBancariaCierre.fromApi(item));
  },
});
