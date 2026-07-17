package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.TipoMovimientoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaAuditLogResponse {
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
}
