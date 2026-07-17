package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.EmpleadoAuditLogResponse;
import FarmaciaERP.Domain.Repositories.IEmpleadoAuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** RRHH.01 - Contratación: historial de auditoría de colaboradores. */
@Service
public class ListarAuditoriaEmpleadosUseCase {

    private final IEmpleadoAuditLogRepository auditLogRepository;

    public ListarAuditoriaEmpleadosUseCase(IEmpleadoAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<EmpleadoAuditLogResponse> todos() {
        return auditLogRepository.findAll().stream().map(EmpleadoResponseAssembler::toAuditResponse).toList();
    }

    public List<EmpleadoAuditLogResponse> porCodigoEmpleado(String codigo) {
        return auditLogRepository.findByCodigoEmpleado(codigo).stream()
                .map(EmpleadoResponseAssembler::toAuditResponse).toList();
    }
}
