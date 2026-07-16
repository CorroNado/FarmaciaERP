package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.PartidaPresupuestal;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PartidaPresupuestalJPA;

public class PartidaPresupuestalMapper {

    public static PartidaPresupuestalJPA ToEntity(PartidaPresupuestal partida, CentroCostoJPA centroCostoRef) {
        PartidaPresupuestalJPA entity = new PartidaPresupuestalJPA();
        entity.setId(partida.getId());
        entity.setCodigo(partida.getCodigo());
        entity.setNombre(partida.getNombre());
        entity.setCentroCosto(centroCostoRef);
        entity.setMontoPresupuestado(partida.getMontoPresupuestado());
        entity.setActiva(partida.isActiva());
        return entity;
    }

    public static PartidaPresupuestal ToDomain(PartidaPresupuestalJPA entity) {
        return PartidaPresupuestal.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                CentroCostoMapper.ToDomain(entity.getCentroCosto()),
                entity.getMontoPresupuestado(),
                entity.isActiva()
        );
    }
}