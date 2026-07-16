package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.InspeccionCalidad;
import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.InspeccionCalidadJPA;

public class InspeccionCalidadMapper {

    /**
     * `entradaMercanciaRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static InspeccionCalidadJPA ToEntity(InspeccionCalidad inspeccion, EntradaMercanciaJPA entradaMercanciaRef) {
        InspeccionCalidadJPA entity = new InspeccionCalidadJPA();
        entity.setId(inspeccion.getId());
        entity.setNumero(inspeccion.getNumero());
        entity.setEntradaMercancia(entradaMercanciaRef);
        entity.setMuestreoConforme(inspeccion.isMuestreoConforme());
        entity.setCadenaFrioConforme(inspeccion.isCadenaFrioConforme());
        entity.setRegistroSanitarioVigente(inspeccion.isRegistroSanitarioVigente());
        entity.setEmpaqueConforme(inspeccion.isEmpaqueConforme());
        entity.setDecision(inspeccion.getDecision());
        entity.setMotivoRechazo(inspeccion.getMotivoRechazo());
        entity.setFecha(inspeccion.getFecha());
        return entity;
    }

    public static InspeccionCalidad ToDomain(InspeccionCalidadJPA entity) {
        return InspeccionCalidad.reconstruir(
                entity.getId(),
                entity.getNumero(),
                EntradaMercanciaMapper.ToDomain(entity.getEntradaMercancia()),
                entity.isMuestreoConforme(),
                entity.isCadenaFrioConforme(),
                entity.isRegistroSanitarioVigente(),
                entity.isEmpaqueConforme(),
                entity.getDecision(),
                entity.getMotivoRechazo(),
                entity.getFecha()
        );
    }
}
