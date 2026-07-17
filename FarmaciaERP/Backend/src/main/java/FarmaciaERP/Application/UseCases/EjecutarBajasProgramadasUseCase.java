package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;
import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import FarmaciaERP.Domain.Repositories.IEmpleadoAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RRHH.01 - Contratación: monitoreo automático de bajas programadas. Inactiva
 * a los colaboradores cuya fecha efectiva de baja programada ya se cumplió
 * (espejo de checkScheduledTerminations del prototipo).
 */
@Service
public class EjecutarBajasProgramadasUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public EjecutarBajasProgramadasUseCase(IEmpleadoRepository empleadoRepository,
                                            IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public int ejecutar() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Empleado> vencidas = empleadoRepository.findConBajaProgramadaVencida(ahora);

        for (Empleado empleado : vencidas) {
            boolean cambio = empleado.ejecutarBajaProgramadaSiCorresponde(ahora);
            if (cambio) {
                empleadoRepository.save(empleado);
                auditLogRepository.save(new EmpleadoAuditLog(
                        "Sistema",
                        empleado.getNombreCompleto(),
                        empleado.getCodigo(),
                        TipoMovimientoRRHH.CAMBIO_AUTOMATICO,
                        "Cambio automático a Inactivo por finalización de turno programado",
                        "Activo",
                        "Inactivo",
                        "Baja programada ejecutada automáticamente."
                ));
            }
        }
        return vencidas.size();
    }
}
