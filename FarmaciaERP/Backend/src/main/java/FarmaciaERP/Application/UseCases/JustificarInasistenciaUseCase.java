package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.JustificarInasistenciaRequest;
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
 * RRHH.02 - Control de Asistencia: justificación de inasistencia (modal
 * "📄 Justificación de Inasistencia" del prototipo).
 */
@Service
public class JustificarInasistenciaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;
    private final IAsistenciaAuditLogRepository auditLogRepository;

    public JustificarInasistenciaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository,
                                          IAsistenciaAuditLogRepository auditLogRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public RegistroAsistenciaResponse ejecutar(Long id, JustificarInasistenciaRequest request) {
        RegistroAsistencia registro = registroAsistenciaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Registro de asistencia no encontrado: " + id));

        String antes = RegistroAsistenciaResponseAssembler.snapshot(registro);
        registro.justificar(request.getMotivo());
        RegistroAsistencia guardado = registroAsistenciaRepository.save(registro);

        auditLogRepository.save(new AsistenciaAuditLog(
                request.getUsuario(),
                guardado.getEmpleado().getNombreCompleto(),
                guardado.getEmpleado().getCodigo(),
                TipoMovimientoAsistencia.JUSTIFICACION,
                "Justificación de inasistencia registrada",
                antes,
                RegistroAsistenciaResponseAssembler.snapshot(guardado),
                request.getMotivo()
        ));

        return RegistroAsistenciaResponseAssembler.toResponse(guardado);
    }
}
