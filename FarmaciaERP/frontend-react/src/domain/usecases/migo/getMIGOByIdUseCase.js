import { EntradaMercancia } from '../../models/EntradaMercancia';

export const getMIGOByIdUseCase = (migoRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la entrada MIGO es requerido');
    const raw = await migoRepository.getById(id);
    return EntradaMercancia.fromApi(raw);
  },
});
