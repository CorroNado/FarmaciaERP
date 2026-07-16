import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const getContabilizacionesARUseCase = (contabilizacionARRepository) => ({
  async execute(filters = {}) {
    const raw = await contabilizacionARRepository.getAll(filters);
    return raw.map((item) => ContabilizacionAR.fromApi(item));
  },
});
