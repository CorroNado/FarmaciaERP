package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.TipoMovimientoAsistencia;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * RRHH.02 - Control de Asistencia: registro de auditoría de cada movimiento
 * realizado sobre un turno de asistencia (programación, checkin, checkout,
 * justificación, edición y eliminación).
 */
@Getter
@Setter
public class AsistenciaAuditLog {

    private Long id;
    private LocalDateTime fecha;
    private String usuario;
    private String colaborador;
    private String codigoEmpleado;
    private TipoMovimientoAsistencia tipo;
    private String detalle;
    private String antes;
    private String despues;
    private String motivo;

    public AsistenciaAuditLog() {
    }

    public AsistenciaAuditLog(String usuario, String colaborador, String codigoEmpleado,
                               TipoMovimientoAsistencia tipo, String detalle, String antes, String despues,
                               String motivo) {
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
        this.colaborador = colaborador;
        this.codigoEmpleado = codigoEmpleado;
        this.tipo = tipo;
        this.detalle = detalle;
        this.antes = antes;
        this.despues = despues;
        this.motivo = motivo;
    }
}
