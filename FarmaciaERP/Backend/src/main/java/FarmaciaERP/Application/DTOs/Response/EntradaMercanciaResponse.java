package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoEntradaMercancia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntradaMercanciaResponse {
    private Long id;
    private String numero;
    private Long ordenCompraId;
    private String numeroOrdenCompra;
    private String razonSocialProveedor;
    private String lote;
    private String fechaVencimiento;
    private double temperaturaArribo;
    private int cantidadPedida;
    private int cantidadRecibida;
    private int diferencia;
    private double porcentajeDiferencia;
    private EstadoEntradaMercancia estado;
    private boolean alertaCadenaFrio;
    private LocalDateTime fecha;
}
