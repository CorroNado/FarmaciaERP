import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const revisarAjusteAsientosUseCase = (contabilizacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la contabilización AR es requerido');
    const raw = await contabilizacionARRepository.revisarAjusteAsientos(id);
    return ContabilizacionAR.fromApi(raw);
  },
});
