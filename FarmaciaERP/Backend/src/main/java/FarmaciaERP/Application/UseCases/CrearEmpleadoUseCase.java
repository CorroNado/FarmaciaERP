package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearEmpleadoRequest;
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
 * RRHH.01 - Contratación: alta de un nuevo colaborador.
 */
@Service
public class CrearEmpleadoUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public CrearEmpleadoUseCase(IEmpleadoRepository empleadoRepository,
                                 IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EmpleadoResponse ejecutar(String usuario, CrearEmpleadoRequest request) {
        if (empleadoRepository.findByDni(request.getDni()).isPresent()) {
            throw new BadRequestException("Ya existe un colaborador con el mismo DNI");
        }
        if (request.getCorreo() != null && !request.getCorreo().isBlank()
                && empleadoRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new BadRequestException("Ya existe un colaborador con el mismo correo electrónico");
        }

        String codigo = empleadoRepository.generarSiguienteCodigo();
        Empleado empleado = new Empleado(
                codigo,
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

        Empleado guardado = empleadoRepository.save(empleado);

        auditLogRepository.save(new EmpleadoAuditLog(
                usuario,
                guardado.getNombreCompleto(),
                guardado.getCodigo(),
                TipoMovimientoRRHH.ALTA,
                "Nuevo empleado registrado",
                "N/A",
                EmpleadoResponseAssembler.snapshot(guardado),
                "Registro inicial"
        ));

        return EmpleadoResponseAssembler.toResponse(guardado);
    }
}
