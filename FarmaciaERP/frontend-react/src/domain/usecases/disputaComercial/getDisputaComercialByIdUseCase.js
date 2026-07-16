import { DisputaComercial } from '../../models/DisputaComercial';

export const getDisputaComercialByIdUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.getById(id);
    return DisputaComercial.fromApi(raw);
  },
});
