package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ProgramarBajaRequest;
import FarmaciaERP.Application.DTOs.Response.EmpleadoResponse;
import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;
import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEmpleadoAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RRHH.01 - Contratación: baja programada para el término del último turno
 * activo del colaborador (caso 2.b de la lógica de bajas inteligentes).
 */
@Service
public class ProgramarBajaUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public ProgramarBajaUseCase(IEmpleadoRepository empleadoRepository,
                                 IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EmpleadoResponse ejecutar(Long id, ProgramarBajaRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + id));

        empleado.programarBaja(request.getFechaEfectiva(), request.getObservacion(), request.getTurnoInfo());
        Empleado actualizado = empleadoRepository.save(empleado);

        auditLogRepository.save(new EmpleadoAuditLog(
                request.getUsuario(),
                actualizado.getNombreCompleto(),
                actualizado.getCodigo(),
                TipoMovimientoRRHH.BAJA_PROGRAMADA,
                "Baja programada para el final del turno " + (request.getTurnoInfo() == null ? "" : request.getTurnoInfo()),
                "Estado: Activo",
                "Baja programada para " + request.getFechaEfectiva(),
                request.getObservacion() != null && !request.getObservacion().isBlank()
                        ? request.getObservacion() : "Programación automática"
        ));

        return EmpleadoResponseAssembler.toResponse(actualizado);
    }
}
