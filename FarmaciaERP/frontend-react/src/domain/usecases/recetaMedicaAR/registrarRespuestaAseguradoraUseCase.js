import { RecetaMedicaAR } from '../../models/RecetaMedicaAR';

export const registrarRespuestaAseguradoraUseCase = (recetaMedicaARRepository) => ({
  async execute(id, { aceptaImpugnacion }) {
    if (!id) throw new Error('El id de la receta médica es requerido');

    const payload = RecetaMedicaAR.toApiRespuesta({ aceptaImpugnacion });
    const raw = await recetaMedicaARRepository.registrarRespuestaAseguradora(id, payload);
    return RecetaMedicaAR.fromApi(raw);
  },
});
