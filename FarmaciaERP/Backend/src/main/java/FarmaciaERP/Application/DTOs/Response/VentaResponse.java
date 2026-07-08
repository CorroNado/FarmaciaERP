package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Domain.Enums.MetodoPago;
import FarmaciaERP.Domain.Enums.TipoComprobante;
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
public class VentaResponse {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private LocalDateTime fecha;
    private EstadoVenta estado;
    private MetodoPago metodoPago;
    private TipoComprobante tipoComprobante;
    private String numeroComprobante;
    private List<DetalleVentaResponse> detalles;
    private double total;
}
