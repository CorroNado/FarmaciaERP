package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Domain.Entities.DebitoAR;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio DebitoAR.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de DebitoAR.
 */
public class DebitoARResponseAssembler {

    public static DebitoARResponse toResponse(DebitoAR debito) {
        return new DebitoARResponse(
                debito.getId(),
                debito.getRecetaMedicaAR().getId(),
                debito.getRecetaMedicaAR().getNumero(),
                debito.getRecetaMedicaAR().getContabilizacionAR().getId(),
                debito.getMonto(),
                debito.getMotivo(),
                debito.getEstado(),
                debito.getJustificado(),
                debito.isTramitado(),
                debito.isAjustado(),
                debito.getFecha(),
                debito.getFechaAjuste(),
                debito.estaConciliado()
        );
    }
}
