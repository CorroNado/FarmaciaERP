package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.AccionDevolucion;
import FarmaciaERP.Domain.Enums.MotivoDevolucion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DevolucionResponse {
    private Long id;
    private Long ventaId;
    private LocalDateTime fecha;
    private MotivoDevolucion motivo;
    private AccionDevolucion accion;
    private List<DetalleDevolucionResponse> detalles;
    private double monto;
    private VentaResponse venta;
}
