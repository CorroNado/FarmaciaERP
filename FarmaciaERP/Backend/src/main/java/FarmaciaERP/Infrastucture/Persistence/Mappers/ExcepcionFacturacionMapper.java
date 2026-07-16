package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ExcepcionFacturacionJPA;

public class ExcepcionFacturacionMapper {

    /**
     * `conciliacionTresViasRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static ExcepcionFacturacionJPA ToEntity(ExcepcionFacturacion excepcion,
                                                     ConciliacionTresViasJPA conciliacionTresViasRef) {
        ExcepcionFacturacionJPA entity = new ExcepcionFacturacionJPA();
        entity.setId(excepcion.getId());
        entity.setNumero(excepcion.getNumero());
        entity.setConciliacionTresVias(conciliacionTresViasRef);
        entity.setMontoFactura(excepcion.getMontoFactura());
        entity.setMontoContrato(excepcion.getMontoContrato());
        entity.setDiferencia(excepcion.getDiferencia());
        entity.setTipoDiscrepancia(excepcion.getTipoDiscrepancia());
        entity.setEstado(excepcion.getEstado());
        entity.setRevisada(excepcion.isRevisada());
        entity.setClasificada(excepcion.isClasificada());
        entity.setNotificada(excepcion.isNotificada());
        entity.setFecha(excepcion.getFecha());
        return entity;
    }

    public static ExcepcionFacturacion ToDomain(ExcepcionFacturacionJPA entity) {
        return ExcepcionFacturacion.reconstruir(
                entity.getId(),
                entity.getNumero(),
                ConciliacionTresViasMapper.ToDomain(entity.getConciliacionTresVias()),
                entity.getMontoFactura(),
                entity.getMontoContrato(),
                entity.getDiferencia(),
                entity.getTipoDiscrepancia(),
                entity.getEstado(),
                entity.isRevisada(),
                entity.isClasificada(),
                entity.isNotificada(),
                entity.getFecha()
        );
    }
}
