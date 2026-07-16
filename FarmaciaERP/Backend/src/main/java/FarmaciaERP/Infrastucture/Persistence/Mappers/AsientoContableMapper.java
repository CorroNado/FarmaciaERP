package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.LineaAsientoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;

import java.util.List;
import java.util.Map;

public class AsientoContableMapper {

    public static AsientoContableJPA ToEntity(AsientoContable domain,
                                              Map<Long, SubcuentaDivisionariaJPA> subcuentasMap,
                                              Map<Long, CentroCostoJPA> centrosCostoMap) {
        AsientoContableJPA entity = new AsientoContableJPA();
        entity.setId(domain.getId());
        entity.setNumero(domain.getNumero());
        entity.setFechaContable(domain.getFechaContable());
        entity.setGlosa(domain.getGlosa());
        entity.setTipoAsiento(domain.getTipoAsiento());
        entity.setEstado(domain.getEstado());

        List<LineaAsientoJPA> lineasJPA = domain.getLineas().stream().map(linea -> {
            SubcuentaDivisionariaJPA subcuentaRef = subcuentasMap.get(linea.getSubcuenta().getId());
            CentroCostoJPA centroCostoRef = linea.getCentroCosto() == null ? null : centrosCostoMap.get(linea.getCentroCosto().getId());
            return LineaAsientoMapper.ToEntity(linea, entity, subcuentaRef, centroCostoRef);
        }).toList();

        entity.getLineas().clear();
        entity.getLineas().addAll(lineasJPA);
        return entity;
    }

    public static AsientoContable ToDomain(AsientoContableJPA entity) {
        List<LineaAsiento> lineasDomain = entity.getLineas().stream()
                .map(LineaAsientoMapper::ToDomain)
                .toList();

        return AsientoContable.reconstruir(
                entity.getId(),
                entity.getNumero(),
                entity.getFechaContable(),
                entity.getGlosa(),
                entity.getTipoAsiento(),
                entity.getEstado(),
                lineasDomain
        );
    }
}