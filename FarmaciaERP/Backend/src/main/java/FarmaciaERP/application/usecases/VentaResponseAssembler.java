package FarmaciaERP.application.usecases;

import FarmaciaERP.application.dto.Response.DetalleVentaResponse;
import FarmaciaERP.application.dto.Response.VentaResponse;
import FarmaciaERP.domain.entities.DetalleVenta;
import FarmaciaERP.domain.entities.Venta;

import java.util.List;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio Venta.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de Venta.
 */
public class VentaResponseAssembler {

    public static VentaResponse toResponse(Venta venta) {
        List<DetalleVentaResponse> detalles = venta.getDetalles().stream()
                .map(VentaResponseAssembler::toDetalleResponse)
                .toList();

        return new VentaResponse(
                venta.getId(),
                venta.getCliente().getId(),
                venta.getCliente().getNombres().getValor(),
                venta.getFecha(),
                venta.getEstado(),
                venta.getMetodoPago(),
                venta.getTipoComprobante(),
                venta.getNumeroComprobante(),
                detalles,
                venta.getTotal()
        );
    }

    private static DetalleVentaResponse toDetalleResponse(DetalleVenta detalle) {
        return new DetalleVentaResponse(
                detalle.getMedicamento().getId(),
                detalle.getMedicamento().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
