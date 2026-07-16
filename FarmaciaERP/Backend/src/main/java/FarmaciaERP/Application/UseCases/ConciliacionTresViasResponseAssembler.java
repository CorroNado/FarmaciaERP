package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ConciliacionTresViasResponse;
import FarmaciaERP.Domain.Entities.ConciliacionTresVias;

public class ConciliacionTresViasResponseAssembler {

    public static ConciliacionTresViasResponse toResponse(ConciliacionTresVias conciliacion) {
        return new ConciliacionTresViasResponse(
                conciliacion.getId(),
                conciliacion.getNumero(),
                conciliacion.getOrdenCompra().getId(),
                conciliacion.getOrdenCompra().getNumero(),
                conciliacion.getOrdenCompra().getProveedor().getRazonSocial(),
                conciliacion.getEntradaMercancia().getId(),
                conciliacion.getEntradaMercancia().getNumero(),
                conciliacion.getFacturaMIRO().getId(),
                conciliacion.getFacturaMIRO().getNumeroFactura(),
                conciliacion.isCantidadCoincide(),
                conciliacion.isPrecioCoincide(),
                conciliacion.isFacturaVinculada(),
                conciliacion.isQaAprobado(),
                conciliacion.getResultado(),
                conciliacion.getFecha()
        );
    }
}
