package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;
import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEmpleadoAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RRHH.01 - Contratación: eliminación definitiva de un colaborador del sistema.
 * Nota: la limpieza de sus registros de asistencia corresponde al módulo de
 * Asistencia (aún no implementado) al recibir este evento.
 */
@Service
public class EliminarEmpleadoUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IEmpleadoAuditLogRepository auditLogRepository;

    public EliminarEmpleadoUseCase(IEmpleadoRepository empleadoRepository,
                                    IEmpleadoAuditLogRepository auditLogRepository) {
        this.empleadoRepository = empleadoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void ejecutar(Long id, String usuario) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + id));

        String antes = EmpleadoResponseAssembler.snapshot(empleado);

        auditLogRepository.save(new EmpleadoAuditLog(
                usuario,
                empleado.getNombreCompleto(),
                empleado.getCodigo(),
                TipoMovimientoRRHH.ELIMINACION,
                "Empleado eliminado del sistema",
                antes,
                "N/A",
                "Eliminación de colaborador"
        ));

        empleadoRepository.deleteById(id);
    }
}
