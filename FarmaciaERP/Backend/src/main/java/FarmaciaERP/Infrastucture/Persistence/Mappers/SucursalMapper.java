package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;

public class SucursalMapper {

    public static SucursalJPA ToEntity(Sucursal sucursal) {
        SucursalJPA entity = new SucursalJPA();
        entity.setId(sucursal.getId());
        entity.setCodigo(sucursal.getCodigo());
        entity.setNombre(sucursal.getNombre());
        entity.setActiva(sucursal.isActiva());
        return entity;
    }

    public static Sucursal ToDomain(SucursalJPA entity) {
        return Sucursal.reconstruir(entity.getId(), entity.getCodigo(), entity.getNombre(), entity.isActiva());
    }
}
