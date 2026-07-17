import { AsistenciaAuditLog } from '../../models/RegistroAsistencia';

export const getAuditoriaAsistenciaUseCase = (asistenciaRepository) => ({
  async execute(codigoEmpleado) {
    const raw = await asistenciaRepository.auditoria(codigoEmpleado);
    return raw.map((item) => AsistenciaAuditLog.fromApi(item));
  },
});
