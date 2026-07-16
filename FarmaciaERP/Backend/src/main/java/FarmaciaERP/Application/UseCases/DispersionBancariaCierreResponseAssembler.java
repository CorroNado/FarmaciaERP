package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;

public class DispersionBancariaCierreResponseAssembler {

    public static DispersionBancariaCierreResponse toResponse(DispersionBancariaCierre dispersion) {
        return new DispersionBancariaCierreResponse(
                dispersion.getId(),
                dispersion.getNumero(),
                dispersion.getPropuestaPagoAutomatica().getId(),
                dispersion.getPropuestaPagoAutomatica().getNumero(),
                dispersion.getMontoDispersion(),
                dispersion.isPropuestaCompilada(),
                dispersion.getPropuestaValidada(),
                dispersion.getIntentosValidacion(),
                dispersion.isLoteCorregido(),
                dispersion.isArchivoGenerado(),
                dispersion.isFirmado(),
                dispersion.isTransferenciasEjecutadas(),
                dispersion.isExtractoImportado(),
                dispersion.isConciliado(),
                dispersion.isObligacionExtinguida(),
                dispersion.getEstado(),
                dispersion.getFecha()
        );
    }
}
