import { DebitoAR } from '../../models/DebitoAR';

export const aplicarAjusteTecnicoContableUseCase = (debitoARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del débito es requerido');
    const raw = await debitoARRepository.aplicarAjusteTecnico(id);
    return DebitoAR.fromApi(raw);
  },
});
