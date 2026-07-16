import { RecetaMedicaAR } from '../../models/RecetaMedicaAR';

export const validarTroquelesFirmasUseCase = (recetaMedicaARRepository) => ({
  async execute(id, { valido, motivoRechazo }) {
    if (!id) throw new Error('El id de la receta médica es requerido');
    if (!valido && !motivoRechazo?.trim()) {
      throw new Error('Debes registrar el motivo del rechazo cuando la receta no es válida');
    }

    const payload = RecetaMedicaAR.toApiValidar({ valido, motivoRechazo });
    const raw = await recetaMedicaARRepository.validarTroquelesFirmas(id, payload);
    return RecetaMedicaAR.fromApi(raw);
  },
});
