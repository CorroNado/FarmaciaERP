package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;

public class SubcuentaDivisionariaMapper {

    public static SubcuentaDivisionariaJPA ToEntity(SubcuentaDivisionaria subcuenta, CuentaJPA cuentaRef) {
        SubcuentaDivisionariaJPA entity = new SubcuentaDivisionariaJPA();
        entity.setId(subcuenta.getId());
        entity.setCodigo(subcuenta.getCodigo());
        entity.setNombre(subcuenta.getNombre());
        entity.setCuenta(cuentaRef);
        entity.setActiva(subcuenta.isActiva());
        return entity;
    }

    public static SubcuentaDivisionaria ToDomain(SubcuentaDivisionariaJPA entity) {
        return SubcuentaDivisionaria.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                CuentaMapper.ToDomain(entity.getCuenta()),
                entity.isActiva()
        );
    }
}