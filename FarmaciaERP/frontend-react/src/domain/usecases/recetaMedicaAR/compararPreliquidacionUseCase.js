import { RecetaMedicaAR } from '../../models/RecetaMedicaAR';

export const compararPreliquidacionUseCase = (recetaMedicaARRepository) => ({
  async execute(id, { coincide, inconsistencia }) {
    if (!id) throw new Error('El id de la receta médica es requerido');
    if (!coincide && !inconsistencia?.trim()) {
      throw new Error('Debes describir la inconsistencia detectada frente a la pre-liquidación');
    }

    const payload = RecetaMedicaAR.toApiComparar({ coincide, inconsistencia });
    const raw = await recetaMedicaARRepository.compararPreliquidacion(id, payload);
    return RecetaMedicaAR.fromApi(raw);
  },
});
