package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.ResultadoConciliacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConciliacionTresViasResponse {
    private Long id;
    private String numero;
    private Long ordenCompraId;
    private String numeroOrdenCompra;
    private String razonSocialProveedor;
    private Long entradaMercanciaId;
    private String numeroEntradaMercancia;
    private Long facturaMIROId;
    private String numeroFactura;
    private boolean cantidadCoincide;
    private boolean precioCoincide;
    private boolean facturaVinculada;
    private boolean qaAprobado;
    private ResultadoConciliacion resultado;
    private LocalDateTime fecha;
}
