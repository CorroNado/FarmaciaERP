package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DetalleDevolucionResponse;
import FarmaciaERP.Application.DTOs.Response.DevolucionResponse;
import FarmaciaERP.Domain.Entities.DetalleDevolucion;
import FarmaciaERP.Domain.Entities.Devolucion;

import java.util.List;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio Devolucion.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de Devolucion.
 */
public class DevolucionResponseAssembler {

    public static DevolucionResponse toResponse(Devolucion devolucion) {
        List<DetalleDevolucionResponse> detalles = devolucion.getDetalles().stream()
                .map(DevolucionResponseAssembler::toDetalleResponse)
                .toList();

        return new DevolucionResponse(
                devolucion.getId(),
                devolucion.getVenta().getId(),
                devolucion.getFecha(),
                devolucion.getMotivo(),
                devolucion.getAccion(),
                detalles,
                devolucion.getMonto(),
                VentaResponseAssembler.toResponse(devolucion.getVenta())
        );
    }

    private static DetalleDevolucionResponse toDetalleResponse(DetalleDevolucion detalle) {
        return new DetalleDevolucionResponse(
                detalle.getMedicamento().getId(),
                detalle.getMedicamento().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
