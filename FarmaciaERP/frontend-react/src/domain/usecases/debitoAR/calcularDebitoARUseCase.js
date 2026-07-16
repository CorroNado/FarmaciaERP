import { DebitoAR } from '../../models/DebitoAR';

export const calcularDebitoARUseCase = (debitoARRepository) => ({
  async execute({ recetaMedicaARId }) {
    if (!recetaMedicaARId) throw new Error('El id de la receta médica es requerido');
    const payload = DebitoAR.toApiCalcular({ recetaMedicaARId });
    const raw = await debitoARRepository.calcular(payload);
    return DebitoAR.fromApi(raw);
  },
});
