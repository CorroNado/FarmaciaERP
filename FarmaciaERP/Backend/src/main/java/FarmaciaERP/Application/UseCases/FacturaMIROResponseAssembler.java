package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.FacturaMIROResponse;
import FarmaciaERP.Domain.Entities.FacturaMIRO;

public class FacturaMIROResponseAssembler {

    public static FacturaMIROResponse toResponse(FacturaMIRO factura) {
        return new FacturaMIROResponse(
                factura.getId(),
                factura.getNumero(),
                factura.getNumeroFactura(),
                factura.getOrdenCompra().getId(),
                factura.getOrdenCompra().getNumero(),
                factura.getOrdenCompra().getProveedor().getRazonSocial(),
                factura.getFechaEmision(),
                factura.getMontoNeto(),
                factura.getIgv(),
                factura.getMontoTotal(),
                factura.getEstado(),
                factura.getFecha()
        );
    }
}
