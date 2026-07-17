package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;

import java.util.List;

public interface IEmpleadoAuditLogRepository {

    EmpleadoAuditLog save(EmpleadoAuditLog log);

    List<EmpleadoAuditLog> findAll();

    List<EmpleadoAuditLog> findByCodigoEmpleado(String codigoEmpleado);
}
