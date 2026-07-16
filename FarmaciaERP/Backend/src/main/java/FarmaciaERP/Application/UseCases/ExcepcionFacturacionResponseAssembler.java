package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ExcepcionFacturacionResponse;
import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;

public class ExcepcionFacturacionResponseAssembler {

    public static ExcepcionFacturacionResponse toResponse(ExcepcionFacturacion excepcion) {
        return new ExcepcionFacturacionResponse(
                excepcion.getId(),
                excepcion.getNumero(),
                excepcion.getConciliacionTresVias().getId(),
                excepcion.getConciliacionTresVias().getNumero(),
                excepcion.getConciliacionTresVias().getOrdenCompra().getId(),
                excepcion.getConciliacionTresVias().getOrdenCompra().getNumero(),
                excepcion.getConciliacionTresVias().getOrdenCompra().getProveedor().getRazonSocial(),
                excepcion.getConciliacionTresVias().getFacturaMIRO().getId(),
                excepcion.getConciliacionTresVias().getFacturaMIRO().getNumeroFactura(),
                excepcion.getMontoFactura(),
                excepcion.getMontoContrato(),
                excepcion.getDiferencia(),
                excepcion.getTipoDiscrepancia(),
                excepcion.getEstado(),
                excepcion.isRevisada(),
                excepcion.isClasificada(),
                excepcion.isNotificada(),
                excepcion.getFecha()
        );
    }
}
