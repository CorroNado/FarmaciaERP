package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;

public class PropuestaPagoAutomaticaResponseAssembler {

    public static PropuestaPagoAutomaticaResponse toResponse(PropuestaPagoAutomatica propuesta) {
        return new PropuestaPagoAutomaticaResponse(
                propuesta.getId(),
                propuesta.getNumero(),
                propuesta.getLotePagoTesoreria().getId(),
                propuesta.getLotePagoTesoreria().getNumero(),
                propuesta.getMontoPropuesta(),
                propuesta.getSociedad(),
                propuesta.getViaPago(),
                propuesta.getFechaPago(),
                propuesta.isParametrosIntroducidos(),
                propuesta.isPropuestaEjecutada(),
                propuesta.getIntentos(),
                propuesta.getPropuestaCorrecta(),
                propuesta.isPropuestaAprobada(),
                propuesta.isPagoEjecutado(),
                propuesta.isArchivosGenerados(),
                propuesta.getEstado(),
                propuesta.getFecha()
        );
    }
}
