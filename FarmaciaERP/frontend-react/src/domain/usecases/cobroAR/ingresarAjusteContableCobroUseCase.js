import { CobroAR } from '../../models/CobroAR';

export const ingresarAjusteContableCobroUseCase = (cobroARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del cobro es requerido');
    const raw = await cobroARRepository.ajusteDiferencia(id);
    return CobroAR.fromApi(raw);
  },
});
