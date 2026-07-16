package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SD.02.03 / SD.03.01 - Resultado de aceptar una cotización: la cotización
 * queda marcada como ACEPTADA y se genera un pedido de venta (Venta en
 * estado PENDIENTE) a partir de sus detalles.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AceptarCotizacionResponse {
    private CotizacionResponse cotizacion;
    private VentaResponse venta;
}
