package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CambiarEstadoEmpleadoRequest;
import FarmaciaERP.Application.DTOs.Response.EmpleadoResponse;
import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;
import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEmpleadoAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RRHH.01 - Contratación: reactivación de un colaborador inactivo. */
@Service
public class ReactivarEmpleadoUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public ReactivarEmpleadoUseCase(IEmpleadoRepository empleadoRepository,
                                     IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EmpleadoResponse ejecutar(Long id, CambiarEstadoEmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + id));

        empleado.reactivar();
        Empleado actualizado = empleadoRepository.save(empleado);

        auditLogRepository.save(new EmpleadoAuditLog(
                request.getUsuario(),
                actualizado.getNombreCompleto(),
                actualizado.getCodigo(),
                TipoMovimientoRRHH.CAMBIO_ESTADO,
                "Estado cambiado a Activo",
                "Inactivo",
                "Activo",
                "Reactivación"
        ));

        return EmpleadoResponseAssembler.toResponse(actualizado);
    }
}
