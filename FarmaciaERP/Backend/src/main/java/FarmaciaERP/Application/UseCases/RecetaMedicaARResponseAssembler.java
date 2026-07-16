package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio RecetaMedicaAR.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de RecetaMedicaAR.
 */
public class RecetaMedicaARResponseAssembler {

    public static RecetaMedicaARResponse toResponse(RecetaMedicaAR receta) {
        return new RecetaMedicaARResponse(
                receta.getId(),
                receta.getNumero(),
                receta.getContabilizacionAR().getId(),
                receta.getMedicamento(),
                receta.getAseguradora(),
                receta.getMontoDeclarado(),
                receta.getMontoPreliquidado(),
                receta.getEstado(),
                receta.getMotivoRechazo(),
                receta.getInconsistencia(),
                receta.getFecha(),
                receta.estaProcesada(),
                receta.generaDebito()
        );
    }
}
