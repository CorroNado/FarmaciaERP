import { EmpleadoAuditLog } from '../../models/Empleado';

export const getAuditoriaEmpleadosUseCase = (empleadoRepository) => ({
  async execute(codigo) {
    const raw = await empleadoRepository.auditoria(codigo);
    return raw.map((item) => EmpleadoAuditLog.fromApi(item));
  },
});
