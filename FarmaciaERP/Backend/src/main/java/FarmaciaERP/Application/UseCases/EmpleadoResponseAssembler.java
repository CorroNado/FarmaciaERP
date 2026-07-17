package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.EmpleadoAuditLogResponse;
import FarmaciaERP.Application.DTOs.Response.EmpleadoResponse;
import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;

public class EmpleadoResponseAssembler {

    public static EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getCodigo(),
                empleado.getApellidoPaterno(),
                empleado.getApellidoMaterno(),
                empleado.getNombres(),
                empleado.getNombreCompleto(),
                empleado.getDni(),
                empleado.getRol(),
                empleado.getArea(),
                empleado.getFechaIngreso(),
                empleado.getSalario(),
                empleado.getContrato(),
                empleado.getCorreo(),
                empleado.getTelefono(),
                empleado.getEstado(),
                empleado.getBajaProgramadaFechaEfectiva(),
                empleado.getBajaProgramadaObservacion(),
                empleado.getBajaProgramadaTurnoInfo()
        );
    }

    public static EmpleadoAuditLogResponse toAuditResponse(EmpleadoAuditLog log) {
        return new EmpleadoAuditLogResponse(
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

    /** Snapshot legible del colaborador para los campos "antes"/"después" de la auditoría. */
    public static String snapshot(Empleado empleado) {
        return "Estado: " + empleado.getEstado()
                + ", Rol: " + empleado.getRol()
                + ", Contrato: " + empleado.getContrato()
                + ", Salario: " + empleado.getSalario();
    }
}
