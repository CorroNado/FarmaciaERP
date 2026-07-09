package FarmaciaERP.application.dto.Request;

import FarmaciaERP.domain.enums.MetodoPago;
import FarmaciaERP.domain.enums.TipoComprobante;
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
