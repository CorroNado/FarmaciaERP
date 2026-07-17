package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.EliminarRegistroAsistenciaRequest;
import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Enums.TipoMovimientoAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsistenciaAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RRHH.02 - Control de Asistencia: eliminación auditada de un registro
 * (modal "Auditoría de Registro" del prototipo, acción "delete").
 */
@Service
public class EliminarRegistroAsistenciaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;
    private final IAsistenciaAuditLogRepository auditLogRepository;

    public EliminarRegistroAsistenciaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository,
                                              IAsistenciaAuditLogRepository auditLogRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void ejecutar(Long id, EliminarRegistroAsistenciaRequest request) {
        RegistroAsistencia registro = registroAsistenciaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Registro de asistencia no encontrado: " + id));

        if (request.getMotivoAuditoria() == null || request.getMotivoAuditoria().isBlank()) {
            throw new BadRequestException("RN-AST-09: el motivo de auditoría es obligatorio para eliminar el registro");
        }

        String antes = RegistroAsistenciaResponseAssembler.snapshot(registro);
        String colaborador = registro.getEmpleado().getNombreCompleto();
        String codigo = registro.getEmpleado().getCodigo();

        registroAsistenciaRepository.deleteById(id);

        auditLogRepository.save(new AsistenciaAuditLog(
                request.getUsuario(),
                colaborador,
                codigo,
                TipoMovimientoAsistencia.ELIMINACION,
                "Eliminación del registro de asistencia del " + registro.getFecha(),
                antes,
                "Registro eliminado",
                request.getMotivoAuditoria()
        ));
    }
}
