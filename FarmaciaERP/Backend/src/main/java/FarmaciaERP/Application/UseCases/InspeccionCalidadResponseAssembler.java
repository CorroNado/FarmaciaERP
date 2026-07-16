package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.InspeccionCalidadResponse;
import FarmaciaERP.Domain.Entities.InspeccionCalidad;

public class InspeccionCalidadResponseAssembler {

    public static InspeccionCalidadResponse toResponse(InspeccionCalidad inspeccion) {
        return new InspeccionCalidadResponse(
                inspeccion.getId(),
                inspeccion.getNumero(),
                inspeccion.getEntradaMercancia().getId(),
                inspeccion.getEntradaMercancia().getNumero(),
                inspeccion.getEntradaMercancia().getLote(),
                inspeccion.isMuestreoConforme(),
                inspeccion.isCadenaFrioConforme(),
                inspeccion.isRegistroSanitarioVigente(),
                inspeccion.isEmpaqueConforme(),
                inspeccion.getDecision(),
                inspeccion.getMotivoRechazo(),
                inspeccion.getFecha()
        );
    }
}
