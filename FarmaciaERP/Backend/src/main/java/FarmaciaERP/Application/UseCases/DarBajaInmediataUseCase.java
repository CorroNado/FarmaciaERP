package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.BajaInmediataRequest;
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
 * RRHH.01 - Contratación: baja inmediata de un colaborador con turnos activos
 * (caso 2.a de la lógica de bajas inteligentes) - cancela el turno más próximo
 * y exige un motivo.
 */
@Service
public class DarBajaInmediataUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public DarBajaInmediataUseCase(IEmpleadoRepository empleadoRepository,
                                    IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public EmpleadoResponse ejecutar(Long id, BajaInmediataRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + id));

        empleado.darDeBajaInmediata(request.getMotivo());
        Empleado actualizado = empleadoRepository.save(empleado);

        String turnoInfo = (request.getTurnoInfo() == null || request.getTurnoInfo().isBlank())
                ? "No se encontró el turno" : request.getTurnoInfo();

        auditLogRepository.save(new EmpleadoAuditLog(
                request.getUsuario(),
                actualizado.getNombreCompleto(),
                actualizado.getCodigo(),
                TipoMovimientoRRHH.BAJA_INMEDIATA,
                "Baja inmediata - Turno cancelado: " + turnoInfo,
                "Estado: Activo, Turno: " + turnoInfo,
                "Estado: Inactivo",
                request.getMotivo()
        ));

        return EmpleadoResponseAssembler.toResponse(actualizado);
    }
}
