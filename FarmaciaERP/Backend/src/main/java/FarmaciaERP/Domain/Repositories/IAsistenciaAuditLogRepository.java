package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;

import java.util.List;

public interface IAsistenciaAuditLogRepository {

    AsistenciaAuditLog save(AsistenciaAuditLog log);

    List<AsistenciaAuditLog> findAll();

    List<AsistenciaAuditLog> findByCodigoEmpleado(String codigoEmpleado);
}
