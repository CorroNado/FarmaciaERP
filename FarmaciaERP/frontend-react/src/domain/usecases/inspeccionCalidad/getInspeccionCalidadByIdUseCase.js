import { InspeccionCalidad } from '../../models/InspeccionCalidad';

export const getInspeccionCalidadByIdUseCase = (inspeccionCalidadRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la inspección de calidad es requerido');
    const raw = await inspeccionCalidadRepository.getById(id);
    return InspeccionCalidad.fromApi(raw);
  },
});
