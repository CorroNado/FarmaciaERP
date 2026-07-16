package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;

public class CentroCostoMapper {

    public static CentroCostoJPA ToEntity(CentroCosto centroCosto, SucursalJPA sucursalRef) {
        CentroCostoJPA entity = new CentroCostoJPA();
        entity.setId(centroCosto.getId());
        entity.setCodigo(centroCosto.getCodigo());
        entity.setNombre(centroCosto.getNombre());
        entity.setSucursal(sucursalRef);
        entity.setActivo(centroCosto.isActivo());
        return entity;
    }

    public static CentroCosto ToDomain(CentroCostoJPA entity) {
        return CentroCosto.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getSucursal() == null ? null : SucursalMapper.ToDomain(entity.getSucursal()),
                entity.isActivo()
        );
    }
}