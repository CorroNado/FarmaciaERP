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
 * RRHH.02 - Control de Asistencia: marca la salida del colaborador
 * (botón "🚪 Salida" del prototipo), calculando horas trabajadas, horas
 * extras y su factor (1.5x diurno / 2.0x nocturno).
 */
@Service
public class MarcarSalidaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;
    private final IAsistenciaAuditLogRepository auditLogRepository;

    public MarcarSalidaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository,
                                IAsistenciaAuditLogRepository auditLogRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public RegistroAsistenciaResponse ejecutar(Long id, String usuario) {
        RegistroAsistencia registro = registroAsistenciaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Registro de asistencia no encontrado: " + id));

        String antes = "Entrada: " + registro.getHoraEntrada() + ", Salida: --, Horas: --";
        registro.marcarSalida(LocalDateTime.now());
        RegistroAsistencia guardado = registroAsistenciaRepository.save(registro);

        auditLogRepository.save(new AsistenciaAuditLog(
                usuario,
                guardado.getEmpleado().getNombreCompleto(),
                guardado.getEmpleado().getCodigo(),
                TipoMovimientoAsistencia.CHECKOUT,
                "Registro de salida - Hora: " + guardado.getHoraSalida() + ", Horas trabajadas: "
                        + guardado.getHorasTrabajadas() + ", Horas extras: " + guardado.getHorasExtras()
                        + "h (x" + guardado.getFactorExtra() + ")",
                antes,
                "Entrada: " + guardado.getHoraEntrada() + ", Salida: " + guardado.getHoraSalida()
                        + ", Horas: " + guardado.getHorasTrabajadas() + ", Extras: " + guardado.getHorasExtras() + "h",
                ""
        ));

        return RegistroAsistenciaResponseAssembler.toResponse(guardado);
    }
}
