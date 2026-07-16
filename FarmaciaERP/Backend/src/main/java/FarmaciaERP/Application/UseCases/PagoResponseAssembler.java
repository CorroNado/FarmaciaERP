package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PagoResponse;
import FarmaciaERP.Domain.Entities.Pago;

public class PagoResponseAssembler {

    public static PagoResponse toResponse(Pago pago) {
        return new PagoResponse(
                pago.getId(),
                pago.getNumero(),
                pago.getFacturaMIRO().getId(),
                pago.getFacturaMIRO().getNumeroFactura(),
                pago.getFacturaMIRO().getOrdenCompra().getId(),
                pago.getFacturaMIRO().getOrdenCompra().getNumero(),
                pago.getFacturaMIRO().getOrdenCompra().getProveedor().getRazonSocial(),
                pago.getConciliacionTresVias().getId(),
                pago.getBanco(),
                pago.getFechaPago(),
                pago.getMonto(),
                pago.getEstado(),
                pago.getFecha()
        );
    }
}
