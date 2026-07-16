package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.LineaAsientoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;

public class LineaAsientoMapper {

    public static LineaAsientoJPA ToEntity(LineaAsiento domain, AsientoContableJPA asientoRef,
                                           SubcuentaDivisionariaJPA subcuentaRef, CentroCostoJPA centroCostoRef) {
        LineaAsientoJPA entity = new LineaAsientoJPA();
        entity.setId(domain.getId());
        entity.setAsientoContable(asientoRef);
        entity.setSubcuenta(subcuentaRef);
        entity.setCentroCosto(centroCostoRef);
        entity.setDebe(domain.getDebe());
        entity.setHaber(domain.getHaber());
        entity.setGlosaDetalle(domain.getGlosaDetalle());
        return entity;
    }

    public static LineaAsiento ToDomain(LineaAsientoJPA entity) {
        return LineaAsiento.reconstruir(
                entity.getId(),
                SubcuentaDivisionariaMapper.ToDomain(entity.getSubcuenta()),
                entity.getCentroCosto() == null ? null : CentroCostoMapper.ToDomain(entity.getCentroCosto()),
                entity.getDebe(),
                entity.getHaber(),
                entity.getGlosaDetalle()
        );
    }
}