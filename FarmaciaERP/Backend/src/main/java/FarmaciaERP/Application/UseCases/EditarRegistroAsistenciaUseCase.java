package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.EditarRegistroAsistenciaRequest;
import FarmaciaERP.Application.DTOs.Response.RegistroAsistenciaResponse;
import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Enums.TipoMovimientoAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsistenciaAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RRHH.02 - Control de Asistencia: edición auditada de un registro (modal
 * "Auditoría de Registro" del prototipo, acción "edit").
 */
@Service
public class EditarRegistroAsistenciaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;
    private final IAsistenciaAuditLogRepository auditLogRepository;

    public EditarRegistroAsistenciaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository,
                                            IAsistenciaAuditLogRepository auditLogRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public RegistroAsistenciaResponse ejecutar(Long id, EditarRegistroAsistenciaRequest request) {
        RegistroAsistencia registro = registroAsistenciaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Registro de asistencia no encontrado: " + id));

        String antes = RegistroAsistenciaResponseAssembler.snapshot(registro);
        registro.editar(request.getHoraEntrada(), request.getEstado(), request.getMotivoAuditoria());
        RegistroAsistencia guardado = registroAsistenciaRepository.save(registro);

        auditLogRepository.save(new AsistenciaAuditLog(
                request.getUsuario(),
                guardado.getEmpleado().getNombreCompleto(),
                guardado.getEmpleado().getCodigo(),
                TipoMovimientoAsistencia.EDICION,
                "Edición manual del registro de asistencia",
                antes,
                RegistroAsistenciaResponseAssembler.snapshot(guardado),
                request.getMotivoAuditoria()
        ));

        return RegistroAsistenciaResponseAssembler.toResponse(guardado);
    }
}
