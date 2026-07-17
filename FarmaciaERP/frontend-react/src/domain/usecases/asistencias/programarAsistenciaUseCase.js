import { RegistroAsistencia } from '../../models/RegistroAsistencia';

export const programarAsistenciaUseCase = (asistenciaRepository) => ({
  async execute(formData) {
    const payload = RegistroAsistencia.toApiProgramar(formData);
    const raw = await asistenciaRepository.programar(payload);
    return RegistroAsistencia.fromApi(raw);
  },
});
