import { RegistroAsistencia } from '../../models/RegistroAsistencia';

export const getAsistenciasUseCase = (asistenciaRepository) => ({
  async execute(filters = {}) {
    const raw = await asistenciaRepository.getAll(filters);
    return raw.map((item) => RegistroAsistencia.fromApi(item));
  },
});
