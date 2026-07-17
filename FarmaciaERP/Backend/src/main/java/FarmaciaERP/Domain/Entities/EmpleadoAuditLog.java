package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * RRHH.01 - Contratación: registro de auditoría de cada movimiento realizado
 * sobre un colaborador (alta, edición, cambio de estado, bajas y eliminación).
 */
@Getter
@Setter
public class EmpleadoAuditLog {

    private Long id;
    private LocalDateTime fecha;
    private String usuario;
    private String colaborador;
    private String codigoEmpleado;
    private TipoMovimientoRRHH tipo;
    private String detalle;
    private String antes;
    private String despues;
    private String motivo;

    public EmpleadoAuditLog() {
    }

    public EmpleadoAuditLog(String usuario, String colaborador, String codigoEmpleado, TipoMovimientoRRHH tipo,
                             String detalle, String antes, String despues, String motivo) {
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
