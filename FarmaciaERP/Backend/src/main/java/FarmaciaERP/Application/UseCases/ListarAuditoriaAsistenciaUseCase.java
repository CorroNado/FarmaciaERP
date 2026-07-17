package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsistenciaAuditLogResponse;
import FarmaciaERP.Domain.Repositories.IAsistenciaAuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** RRHH.02 - Control de Asistencia: historial de auditoría de los registros. */
@Service
public class ListarAuditoriaAsistenciaUseCase {

    private final IAsistenciaAuditLogRepository auditLogRepository;

    public ListarAuditoriaAsistenciaUseCase(IAsistenciaAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AsistenciaAuditLogResponse> todos() {
        return auditLogRepository.findAll().stream()
                .map(RegistroAsistenciaResponseAssembler::toAuditResponse).toList();
    }

    public List<AsistenciaAuditLogResponse> porCodigoEmpleado(String codigo) {
        return auditLogRepository.findByCodigoEmpleado(codigo).stream()
                .map(RegistroAsistenciaResponseAssembler::toAuditResponse).toList();
    }
}
