package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoCotizacion;
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
public class CotizacionResponse {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private LocalDateTime fecha;
    private int vigenciaDias;
    private LocalDateTime fechaVencimiento;
    private boolean vigente;
    private EstadoCotizacion estado;
    private String motivoRechazo;
    private Long ventaGeneradaId;
    private List<DetalleCotizacionResponse> detalles;
    private double total;
}
