package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoFacturaMIRO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacturaMIROResponse {
    private Long id;
    private String numero;
    private String numeroFactura;
    private Long ordenCompraId;
    private String numeroOrdenCompra;
    private String razonSocialProveedor;
    private String fechaEmision;
    private double montoNeto;
    private double igv;
    private double montoTotal;
    private EstadoFacturaMIRO estado;
    private LocalDateTime fecha;
}
