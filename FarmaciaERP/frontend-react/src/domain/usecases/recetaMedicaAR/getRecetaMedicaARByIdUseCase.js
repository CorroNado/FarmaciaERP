import { RecetaMedicaAR } from '../../models/RecetaMedicaAR';

export const getRecetaMedicaARByIdUseCase = (recetaMedicaARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la receta médica es requerido');
    const raw = await recetaMedicaARRepository.getById(id);
    return RecetaMedicaAR.fromApi(raw);
  },
});
