package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio ContabilizacionAR.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de ContabilizacionAR.
 */
public class ContabilizacionARResponseAssembler {

    public static ContabilizacionARResponse toResponse(ContabilizacionAR contabilizacion) {
        return new ContabilizacionARResponse(
                contabilizacion.getId(),
                contabilizacion.getCierreCaja().getId(),
                contabilizacion.getCierreCaja().getNumero(),
                contabilizacion.getFecha(),
                !Boolean.TRUE.equals(contabilizacion.getCierreCaja().getCuadra()),
                contabilizacion.isConciliacionPOS(),
                contabilizacion.isAsientoProcesado(),
                contabilizacion.isAjusteDescuadre(),
                contabilizacion.isRecetasAuditadas(),
                contabilizacion.getRecetasCorrectas(),
                contabilizacion.getMotivoObservacion(),
                contabilizacion.isSubsanacion(),
                contabilizacion.isConsolidado(),
                contabilizacion.getEstado(),
                contabilizacion.puedeContinuarFase03()
        );
    }
}
