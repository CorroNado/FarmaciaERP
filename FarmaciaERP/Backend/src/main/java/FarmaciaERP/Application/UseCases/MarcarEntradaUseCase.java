package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.RegistroAsistenciaResponse;
import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Enums.TipoMovimientoAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsistenciaAuditLogRepository;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * RRHH.02 - Control de Asistencia: marca la entrada del colaborador
 * (botón "✅ Entrada" del prototipo) y calcula el estado automático de
 * puntualidad.
 */
@Service
public class MarcarEntradaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;
    private final IAsistenciaAuditLogRepository auditLogRepository;

    public MarcarEntradaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository,
                                 IAsistenciaAuditLogRepository auditLogRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public RegistroAsistenciaResponse ejecutar(Long id, String usuario) {
        RegistroAsistencia registro = registroAsistenciaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Registro de asistencia no encontrado: " + id));

        String antes = RegistroAsistenciaResponseAssembler.snapshot(registro);
        registro.marcarEntrada(LocalDateTime.now());
        RegistroAsistencia guardado = registroAsistenciaRepository.save(registro);

        auditLogRepository.save(new AsistenciaAuditLog(
                usuario,
                guardado.getEmpleado().getNombreCompleto(),
                guardado.getEmpleado().getCodigo(),
                TipoMovimientoAsistencia.CHECKIN,
                "Registro de entrada - Hora: " + guardado.getHoraEntrada() + ", Estado: " + guardado.getEstado(),
                antes,
                RegistroAsistenciaResponseAssembler.snapshot(guardado),
                ""
        ));

        return RegistroAsistenciaResponseAssembler.toResponse(guardado);
    }
}
