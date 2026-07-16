package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Domain.Entities.CompensacionAR;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio
 * CompensacionAR. No es un caso de uso en si mismo, es utilitario
 * compartido entre los casos de uso de CompensacionAR.
 */
public class CompensacionARResponseAssembler {

    public static CompensacionARResponse toResponse(CompensacionAR compensacion) {
        return new CompensacionARResponse(
                compensacion.getId(),
                compensacion.getContabilizacionAR().getId(),
                compensacion.isCompensado(),
                compensacion.isReporteGenerado(),
                compensacion.getMontoVentas(),
                compensacion.getMontoAprobadas(),
                compensacion.getPerdidas(),
                compensacion.getMargenNeto(),
                compensacion.getMargenPct(),
                compensacion.isSaldoConfirmado(),
                compensacion.isCerrado(),
                compensacion.getEstado(),
                compensacion.getFecha(),
                compensacion.getFechaCierre(),
                compensacion.estaFinalizado()
        );
    }
}
