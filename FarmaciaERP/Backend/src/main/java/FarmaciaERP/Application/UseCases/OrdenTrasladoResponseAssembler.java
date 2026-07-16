package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.OrdenTrasladoResponse;
import FarmaciaERP.Domain.Entities.OrdenTraslado;

public class OrdenTrasladoResponseAssembler {

    public static OrdenTrasladoResponse toResponse(OrdenTraslado orden) {
        return new OrdenTrasladoResponse(
                orden.getId(),
                orden.getNumero(),
                orden.getInspeccionCalidad().getId(),
                orden.getInspeccionCalidad().getEntradaMercancia().getLote(),
                orden.getSucursalDestino().getId(),
                orden.getSucursalDestino().getNombre(),
                orden.getGuiaRemision(),
                orden.getEstado(),
                orden.getFechaDespacho(),
                orden.getFechaRecepcion()
        );
    }
}
