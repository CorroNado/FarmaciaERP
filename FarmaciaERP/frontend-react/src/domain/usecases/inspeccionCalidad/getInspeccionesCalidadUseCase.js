import { InspeccionCalidad } from '../../models/InspeccionCalidad';

export const getInspeccionesCalidadUseCase = (inspeccionCalidadRepository) => ({
  async execute(filters = {}) {
    const raw = await inspeccionCalidadRepository.getAll(filters);
    return raw.map((item) => InspeccionCalidad.fromApi(item));
  },
});
