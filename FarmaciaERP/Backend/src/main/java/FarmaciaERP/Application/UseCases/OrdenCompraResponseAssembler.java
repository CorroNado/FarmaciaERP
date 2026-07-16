package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DetalleOrdenCompraResponse;
import FarmaciaERP.Application.DTOs.Response.OrdenCompraResponse;
import FarmaciaERP.Domain.Entities.DetalleOrdenCompra;
import FarmaciaERP.Domain.Entities.OrdenCompra;

import java.util.List;

public class OrdenCompraResponseAssembler {

    public static OrdenCompraResponse toResponse(OrdenCompra oc) {
        List<DetalleOrdenCompraResponse> detalles = oc.getDetalles().stream()
                .map(OrdenCompraResponseAssembler::toDetalleResponse)
                .toList();

        return new OrdenCompraResponse(
                oc.getId(),
                oc.getNumero(),
                oc.getSolicitudPedido().getId(),
                oc.getSolicitudPedido().getNumero(),
                oc.getProveedor().getId(),
                oc.getProveedor().getRazonSocial(),
                oc.getConvenio().getId(),
                detalles,
                oc.getMontoTotal(),
                oc.getFecha(),
                oc.getFechaEntregaLimite(),
                oc.getCentroDestino(),
                oc.getEstado(),
                oc.getFechaFirma()
        );
    }

    private static DetalleOrdenCompraResponse toDetalleResponse(DetalleOrdenCompra detalle) {
        return new DetalleOrdenCompraResponse(
                detalle.getMedicamento().getId(),
                detalle.getMedicamento().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
