package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
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
public class OrdenCompraResponse {
    private Long id;
    private String numero;
    private Long solPedId;
    private String numeroSolPed;
    private Long proveedorId;
    private String razonSocialProveedor;
    private Long convenioId;
    private List<DetalleOrdenCompraResponse> detalles;
    private double montoTotal;
    private LocalDateTime fecha;
    private String fechaEntregaLimite;
    private String centroDestino;
    private EstadoOrdenCompra estado;
    private LocalDateTime fechaFirma;
}
