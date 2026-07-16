package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoExcepcionFacturacion;
import FarmaciaERP.Domain.Enums.TipoDiscrepancia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcepcionFacturacionResponse {
    private Long id;
    private String numero;
    private Long conciliacionTresViasId;
    private String numeroConciliacion;
    private Long ordenCompraId;
    private String numeroOrdenCompra;
    private String razonSocialProveedor;
    private Long facturaMIROId;
    private String numeroFactura;
    private double montoFactura;
    private double montoContrato;
    private double diferencia;
    private TipoDiscrepancia tipoDiscrepancia;
    private EstadoExcepcionFacturacion estado;
    private boolean revisada;
    private boolean clasificada;
    private boolean notificada;
    private LocalDateTime fecha;
}
