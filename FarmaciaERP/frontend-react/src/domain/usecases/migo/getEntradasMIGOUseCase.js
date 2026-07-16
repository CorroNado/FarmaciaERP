import { EntradaMercancia } from '../../models/EntradaMercancia';

export const getEntradasMIGOUseCase = (migoRepository) => ({
  async execute(filters = {}) {
    const raw = await migoRepository.getAll(filters);
    return raw.map((item) => EntradaMercancia.fromApi(item));
  },
});
