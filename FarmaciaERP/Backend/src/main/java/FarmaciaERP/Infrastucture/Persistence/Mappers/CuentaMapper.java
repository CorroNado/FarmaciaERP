package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;

public class CuentaMapper {

    public static CuentaJPA ToEntity(Cuenta cuenta) {
        CuentaJPA entity = new CuentaJPA();
        entity.setId(cuenta.getId());
        entity.setCodigo(cuenta.getCodigo());
        entity.setNombre(cuenta.getNombre());
        entity.setTipoCuenta(cuenta.getTipoCuenta());
        entity.setNaturaleza(cuenta.getNaturaleza());
        entity.setActiva(cuenta.isActiva());
        return entity;
    }

    public static Cuenta ToDomain(CuentaJPA entity) {
        return Cuenta.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getTipoCuenta(),
                entity.getNaturaleza(),
                entity.isActiva()
        );
    }
}