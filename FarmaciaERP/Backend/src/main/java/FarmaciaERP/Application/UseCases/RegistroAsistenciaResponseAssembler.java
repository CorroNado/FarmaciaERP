package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsistenciaAuditLogResponse;
import FarmaciaERP.Application.DTOs.Response.RegistroAsistenciaResponse;
import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;

public class RegistroAsistenciaResponseAssembler {

    public static RegistroAsistenciaResponse toResponse(RegistroAsistencia registro) {
        return new RegistroAsistenciaResponse(
                registro.getId(),
                registro.getEmpleado().getId(),
                registro.getEmpleado().getCodigo(),
                registro.getEmpleado().getNombreCompleto(),
                registro.getEmpleado().getRol().name(),
                registro.getFecha(),
                registro.getTurno(),
                registro.getHoraEntrada(),
                registro.getHoraSalida(),
                registro.getHorasTrabajadas(),
                registro.getHorasExtras(),
                registro.getFactorExtra(),
                registro.getEstado(),
                registro.isRegistrado(),
                registro.isJustificado(),
                registro.getMotivoJustificacion(),
                registro.getFechaCreacion()
        );
    }

    public static AsistenciaAuditLogResponse toAuditResponse(AsistenciaAuditLog log) {
        return new AsistenciaAuditLogResponse(
                log.getId(),
                log.getFecha(),
                log.getUsuario(),
                log.getColaborador(),
                log.getCodigoEmpleado(),
                log.getTipo(),
                log.getDetalle(),
                log.getAntes(),
                log.getDespues(),
                log.getMotivo()
        );
    }

    /** Snapshot legible del registro para los campos "antes"/"después" de la auditoría. */
    public static String snapshot(RegistroAsistencia registro) {
        return "Turno: " + registro.getTurno().getDescripcion()
                + ", Hora: " + (registro.getHoraEntrada() != null ? registro.getHoraEntrada() : "--:--:--")
                + ", Estado: " + registro.getEstado();
    }
}
