package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponse {
    private Long id;
    private String numero;
    private Long facturaMIROId;
    private String numeroFactura;
    private Long ordenCompraId;
    private String numeroOrdenCompra;
    private String razonSocialProveedor;
    private Long conciliacionTresViasId;
    private String banco;
    private String fechaPago;
    private double monto;
    private EstadoPago estado;
    private LocalDateTime fecha;
}
