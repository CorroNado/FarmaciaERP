package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoAuditLogResponse {
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
}
