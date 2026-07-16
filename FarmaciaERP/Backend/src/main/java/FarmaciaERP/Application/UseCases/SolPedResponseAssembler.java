package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DetalleSolPedResponse;
import FarmaciaERP.Application.DTOs.Response.SolPedResponse;
import FarmaciaERP.Domain.Entities.DetalleSolPed;
import FarmaciaERP.Domain.Entities.SolicitudPedido;

import java.util.List;

public class SolPedResponseAssembler {

    public static SolPedResponse toResponse(SolicitudPedido solPed) {
        List<DetalleSolPedResponse> detalles = solPed.getDetalles().stream()
                .map(SolPedResponseAssembler::toDetalleResponse)
                .toList();

        return new SolPedResponse(
                solPed.getId(),
                solPed.getNumero(),
                solPed.getFecha(),
                solPed.getResponsable(),
                solPed.getCentroCosto(),
                solPed.getPresupuesto(),
                detalles,
                solPed.getTotal(),
                solPed.getEstado(),
                solPed.getProveedor() != null ? solPed.getProveedor().getId() : null,
                solPed.getProveedor() != null ? solPed.getProveedor().getRazonSocial() : null,
                solPed.getConvenio() != null ? solPed.getConvenio().getId() : null,
                solPed.getConvenio() != null ? solPed.getConvenio().getNumero() : null,
                solPed.getMotivoRechazo()
        );
    }

    private static DetalleSolPedResponse toDetalleResponse(DetalleSolPed detalle) {
        return new DetalleSolPedResponse(
                detalle.getMedicamento().getId(),
                detalle.getMedicamento().getNombre(),
                detalle.getStockActual(),
                detalle.getStockMinimo(),
                detalle.getCantidadSugerida(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
