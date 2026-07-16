package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Domain.Entities.PartidaPresupuestal;

public class PartidaPresupuestalResponseAssembler {

    public static PartidaPresupuestalResponse toResponse(PartidaPresupuestal partida) {
        return new PartidaPresupuestalResponse(
                partida.getId(),
                partida.getCodigo(),
                partida.getNombre(),
                partida.getCentroCosto().getId(),
                partida.getMontoPresupuestado(),
                partida.isActiva()
        );
    }
}