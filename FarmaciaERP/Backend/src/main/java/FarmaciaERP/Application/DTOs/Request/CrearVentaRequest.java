package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.MetodoPago;
import FarmaciaERP.Domain.Enums.TipoComprobante;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearVentaRequest {
    private Long clienteId;
    private MetodoPago metodoPago;
    private TipoComprobante tipoComprobante;
    private List<DetalleVentaRequest> detalles;
}
