package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ActualizarEmpleadoRequest;
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
 * RRHH.01 - Contratación: edición de los datos de un colaborador existente.
 */
@Service
public class ActualizarEmpleadoUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public ActualizarEmpleadoUseCase(IEmpleadoRepository empleadoRepository,
                                      IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EmpleadoResponse ejecutar(Long id, String usuario, ActualizarEmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + id));

        empleadoRepository.findByDni(request.getDni())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> { throw new BadRequestException("Ya existe un colaborador con el mismo DNI"); });

        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            empleadoRepository.findByCorreo(request.getCorreo())
                    .filter(otro -> !otro.getId().equals(id))
                    .ifPresent(otro -> { throw new BadRequestException("Ya existe un colaborador con el mismo correo electrónico"); });
        }

        String antes = EmpleadoResponseAssembler.snapshot(empleado);

        empleado.actualizarDatos(
                request.getApellidoPaterno(),
                request.getApellidoMaterno(),
                request.getNombres(),
                request.getDni(),
                request.getRol(),
                request.getArea(),
                request.getFechaIngreso(),
                request.getSalario(),
                request.getContrato(),
                request.getCorreo(),
                request.getTelefono()
        );

        Empleado actualizado = empleadoRepository.save(empleado);

        auditLogRepository.save(new EmpleadoAuditLog(
                usuario,
                actualizado.getNombreCompleto(),
                actualizado.getCodigo(),
                TipoMovimientoRRHH.EDICION,
                "Datos de empleado actualizados",
                antes,
                EmpleadoResponseAssembler.snapshot(actualizado),
                "Actualización de datos"
        ));

        return EmpleadoResponseAssembler.toResponse(actualizado);
    }
}
