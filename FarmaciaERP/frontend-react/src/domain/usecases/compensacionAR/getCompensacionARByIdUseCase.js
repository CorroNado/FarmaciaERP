import { CompensacionAR } from '../../models/CompensacionAR';

export const getCompensacionARByIdUseCase = (compensacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la compensación final es requerido');
    const raw = await compensacionARRepository.getById(id);
    return CompensacionAR.fromApi(raw);
  },
});
