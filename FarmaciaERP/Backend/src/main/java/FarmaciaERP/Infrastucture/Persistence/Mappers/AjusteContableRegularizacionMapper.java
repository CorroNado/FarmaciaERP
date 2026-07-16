package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Infrastucture.Persistence.Entities.AjusteContableRegularizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DisputaComercialJPA;

public class AjusteContableRegularizacionMapper {

    /**
     * `disputaComercialRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static AjusteContableRegularizacionJPA ToEntity(AjusteContableRegularizacion ajuste,
                                                             DisputaComercialJPA disputaComercialRef) {
        AjusteContableRegularizacionJPA entity = new AjusteContableRegularizacionJPA();
        entity.setId(ajuste.getId());
        entity.setNumero(ajuste.getNumero());
        entity.setDisputaComercial(disputaComercialRef);
        entity.setMontoRegularizacion(ajuste.getMontoRegularizacion());
        entity.setRecibeNotaCredito(ajuste.getRecibeNotaCredito());
        entity.setReclamoGestionado(ajuste.isReclamoGestionado());
        entity.setNotaCreditoEnviadaProveedor(ajuste.isNotaCreditoEnviadaProveedor());
        entity.setNotaCreditoRegistrada(ajuste.isNotaCreditoRegistrada());
        entity.setAsientoRegularizacion(ajuste.isAsientoRegularizacion());
        entity.setDesbloqueado(ajuste.isDesbloqueado());
        entity.setRegularizada(ajuste.isRegularizada());
        entity.setEstado(ajuste.getEstado());
        entity.setFecha(ajuste.getFecha());
        return entity;
    }

    public static AjusteContableRegularizacion ToDomain(AjusteContableRegularizacionJPA entity) {
        return AjusteContableRegularizacion.reconstruir(
                entity.getId(),
                entity.getNumero(),
                DisputaComercialMapper.ToDomain(entity.getDisputaComercial()),
                entity.getMontoRegularizacion(),
                entity.getRecibeNotaCredito(),
                entity.isReclamoGestionado(),
                entity.isNotaCreditoEnviadaProveedor(),
                entity.isNotaCreditoRegistrada(),
                entity.isAsientoRegularizacion(),
                entity.isDesbloqueado(),
                entity.isRegularizada(),
                entity.getEstado(),
                entity.getFecha()
        );
    }
}
