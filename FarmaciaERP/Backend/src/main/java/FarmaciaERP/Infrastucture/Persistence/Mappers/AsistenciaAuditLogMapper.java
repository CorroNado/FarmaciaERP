package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsistenciaAuditLogJPA;

public class AsistenciaAuditLogMapper {

    public static AsistenciaAuditLogJPA ToEntity(AsistenciaAuditLog log) {
        AsistenciaAuditLogJPA entity = new AsistenciaAuditLogJPA();
        if (log.getId() != null) {
            entity.setId(log.getId());
        }
        entity.setFecha(log.getFecha());
        entity.setUsuario(log.getUsuario());
        entity.setColaborador(log.getColaborador());
        entity.setCodigoEmpleado(log.getCodigoEmpleado());
        entity.setTipo(log.getTipo());
        entity.setDetalle(log.getDetalle());
        entity.setAntes(log.getAntes());
        entity.setDespues(log.getDespues());
        entity.setMotivo(log.getMotivo());
        return entity;
    }

    public static AsistenciaAuditLog ToDomain(AsistenciaAuditLogJPA entity) {
        AsistenciaAuditLog log = new AsistenciaAuditLog();
        log.setId(entity.getId());
        log.setFecha(entity.getFecha());
        log.setUsuario(entity.getUsuario());
        log.setColaborador(entity.getColaborador());
        log.setCodigoEmpleado(entity.getCodigoEmpleado());
        log.setTipo(entity.getTipo());
        log.setDetalle(entity.getDetalle());
        log.setAntes(entity.getAntes());
        log.setDespues(entity.getDespues());
        log.setMotivo(entity.getMotivo());
        return log;
    }
}
