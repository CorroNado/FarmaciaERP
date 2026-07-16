package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Infrastucture.Persistence.Entities.DisputaComercialJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ExcepcionFacturacionJPA;

public class DisputaComercialMapper {

    /**
     * `excepcionFacturacionRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static DisputaComercialJPA ToEntity(DisputaComercial disputa, ExcepcionFacturacionJPA excepcionFacturacionRef) {
        DisputaComercialJPA entity = new DisputaComercialJPA();
        entity.setId(disputa.getId());
        entity.setNumero(disputa.getNumero());
        entity.setExcepcionFacturacion(excepcionFacturacionRef);
        entity.setCotejada(disputa.isCotejada());
        entity.setCuantificada(disputa.isCuantificada());
        entity.setImpactoFinanciero(disputa.getImpactoFinanciero());
        entity.setValidadaDesviacion(disputa.isValidadaDesviacion());
        entity.setDisputaAbierta(disputa.isDisputaAbierta());
        entity.setRondaNegociacion(disputa.getRondaNegociacion());
        entity.setMontoContraoferta(disputa.getMontoContraoferta());
        entity.setAbsorbeAceptado(disputa.getAbsorbeAceptado());
        entity.setResueltaWorkflow(disputa.isResueltaWorkflow());
        entity.setEstado(disputa.getEstado());
        entity.setFecha(disputa.getFecha());
        return entity;
    }

    public static DisputaComercial ToDomain(DisputaComercialJPA entity) {
        return DisputaComercial.reconstruir(
                entity.getId(),
                entity.getNumero(),
                ExcepcionFacturacionMapper.ToDomain(entity.getExcepcionFacturacion()),
                entity.isCotejada(),
                entity.isCuantificada(),
                entity.getImpactoFinanciero(),
                entity.isValidadaDesviacion(),
                entity.isDisputaAbierta(),
                entity.getRondaNegociacion(),
                entity.getMontoContraoferta(),
                entity.getAbsorbeAceptado(),
                entity.isResueltaWorkflow(),
                entity.getEstado(),
                entity.getFecha()
        );
    }
}
