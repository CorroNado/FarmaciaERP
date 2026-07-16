package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CotizacionResponse;
import FarmaciaERP.Application.DTOs.Response.DetalleCotizacionResponse;
import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Entities.DetalleCotizacion;

import java.util.List;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio Cotizacion.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de Cotizacion.
 */
public class CotizacionResponseAssembler {

    public static CotizacionResponse toResponse(Cotizacion cotizacion) {
        List<DetalleCotizacionResponse> detalles = cotizacion.getDetalles().stream()
                .map(CotizacionResponseAssembler::toDetalleResponse)
                .toList();

        return new CotizacionResponse(
                cotizacion.getId(),
                cotizacion.getCliente().getId(),
                cotizacion.getCliente().getNombres().getValue(),
                cotizacion.getFecha(),
                cotizacion.getVigenciaDias(),
                cotizacion.getFechaVencimiento(),
                cotizacion.estaVigente(),
                cotizacion.getEstado(),
                cotizacion.getMotivoRechazo(),
                cotizacion.getVentaGeneradaId(),
                detalles,
                cotizacion.getTotal()
        );
    }

    private static DetalleCotizacionResponse toDetalleResponse(DetalleCotizacion detalle) {
        return new DetalleCotizacionResponse(
                detalle.getMedicamento().getId(),
                detalle.getMedicamento().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
