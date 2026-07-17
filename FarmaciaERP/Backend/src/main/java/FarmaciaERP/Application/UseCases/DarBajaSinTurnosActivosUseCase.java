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

/**
 * RRHH.01 - Contratación: baja directa de un colaborador que no tiene
 * turnos activos vigentes (caso 1 de la lógica de bajas inteligentes).
 */
@Service
public class DarBajaSinTurnosActivosUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public DarBajaSinTurnosActivosUseCase(IEmpleadoRepository empleadoRepository,
                                           IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EmpleadoResponse ejecutar(Long id, CambiarEstadoEmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + id));

        empleado.darDeBajaSinTurnosActivos();
        Empleado actualizado = empleadoRepository.save(empleado);

        auditLogRepository.save(new EmpleadoAuditLog(
                request.getUsuario(),
                actualizado.getNombreCompleto(),
                actualizado.getCodigo(),
                TipoMovimientoRRHH.CAMBIO_ESTADO,
                "Estado cambiado a Inactivo (sin turnos activos)",
                "Activo",
                "Inactivo",
                "Baja sin turnos activos"
        ));

        return EmpleadoResponseAssembler.toResponse(actualizado);
    }
}
