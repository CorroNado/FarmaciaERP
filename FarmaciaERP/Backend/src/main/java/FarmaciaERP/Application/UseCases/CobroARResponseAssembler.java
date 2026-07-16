package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Domain.Entities.CobroAR;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio CobroAR.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de CobroAR.
 */
public class CobroARResponseAssembler {

    public static CobroARResponse toResponse(CobroAR cobro) {
        return new CobroARResponse(
                cobro.getId(),
                cobro.getContabilizacionAR().getId(),
                cobro.getMontoTransferido(),
                cobro.getRetenciones(),
                cobro.getComisionPct(),
                cobro.getMontoConciliado(),
                cobro.getDiferencia(),
                cobro.getCuadra(),
                cobro.isRegistrado(),
                cobro.getEstado(),
                cobro.getFecha(),
                cobro.getFechaRegistro(),
                cobro.puedeContinuarFase06()
        );
    }
}
