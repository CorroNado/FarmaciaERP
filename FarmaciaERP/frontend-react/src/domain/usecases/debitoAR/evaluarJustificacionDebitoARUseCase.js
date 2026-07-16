import { DebitoAR } from '../../models/DebitoAR';

export const evaluarJustificacionDebitoARUseCase = (debitoARRepository) => ({
  async execute(id, { justificado }) {
    if (!id) throw new Error('El id del débito es requerido');
    const payload = DebitoAR.toApiEvaluarJustificacion({ justificado });
    const raw = await debitoARRepository.evaluarJustificacion(id, payload);
    return DebitoAR.fromApi(raw);
  },
});
