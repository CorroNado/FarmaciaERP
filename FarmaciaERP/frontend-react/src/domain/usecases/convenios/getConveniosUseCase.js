import { Convenio } from '../../models/Convenio';

export const getConveniosUseCase = (convenioRepository) => ({
  async execute(filters = {}) {
    const raw = await convenioRepository.getAll(filters);
    return raw.map((item) => Convenio.fromApi(item));
  },
});
