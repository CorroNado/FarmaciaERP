import { DisputaComercial } from '../../models/DisputaComercial';

export const getDisputasComercialesUseCase = (disputaComercialRepository) => ({
  async execute(filters = {}) {
    const raw = await disputaComercialRepository.getAll(filters);
    return raw.map((item) => DisputaComercial.fromApi(item));
  },
});
