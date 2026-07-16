package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;
import FarmaciaERP.Infrastucture.Persistence.Entities.DispersionBancariaCierreJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PropuestaPagoAutomaticaJPA;

public class DispersionBancariaCierreMapper {

    /**
     * `propuestaPagoAutomaticaRef` debe ser una instancia YA GESTIONADA
     * por JPA.
     */
    public static DispersionBancariaCierreJPA ToEntity(DispersionBancariaCierre dispersion,
                                                         PropuestaPagoAutomaticaJPA propuestaPagoAutomaticaRef) {
        DispersionBancariaCierreJPA entity = new DispersionBancariaCierreJPA();
        entity.setId(dispersion.getId());
        entity.setNumero(dispersion.getNumero());
        entity.setPropuestaPagoAutomatica(propuestaPagoAutomaticaRef);
        entity.setMontoDispersion(dispersion.getMontoDispersion());
        entity.setPropuestaCompilada(dispersion.isPropuestaCompilada());
        entity.setPropuestaValidada(dispersion.getPropuestaValidada());
        entity.setIntentosValidacion(dispersion.getIntentosValidacion());
        entity.setLoteCorregido(dispersion.isLoteCorregido());
        entity.setArchivoGenerado(dispersion.isArchivoGenerado());
        entity.setFirmado(dispersion.isFirmado());
        entity.setTransferenciasEjecutadas(dispersion.isTransferenciasEjecutadas());
        entity.setExtractoImportado(dispersion.isExtractoImportado());
        entity.setConciliado(dispersion.isConciliado());
        entity.setObligacionExtinguida(dispersion.isObligacionExtinguida());
        entity.setEstado(dispersion.getEstado());
        entity.setFecha(dispersion.getFecha());
        return entity;
    }

    public static DispersionBancariaCierre ToDomain(DispersionBancariaCierreJPA entity) {
        return DispersionBancariaCierre.reconstruir(
                entity.getId(),
                entity.getNumero(),
                PropuestaPagoAutomaticaMapper.ToDomain(entity.getPropuestaPagoAutomatica()),
                entity.getMontoDispersion(),
                entity.isPropuestaCompilada(),
                entity.getPropuestaValidada(),
                entity.getIntentosValidacion(),
                entity.isLoteCorregido(),
                entity.isArchivoGenerado(),
                entity.isFirmado(),
                entity.isTransferenciasEjecutadas(),
                entity.isExtractoImportado(),
                entity.isConciliado(),
                entity.isObligacionExtinguida(),
                entity.getEstado(),
                entity.getFecha()
        );
    }
}
