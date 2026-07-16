package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Infrastucture.Persistence.Entities.CierreCajaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;

public class ContabilizacionARMapper {

    /**
     * cierreCajaRef debe ser una referencia YA GESTIONADA por JPA.
     */
    public static ContabilizacionARJPA ToEntity(ContabilizacionAR contabilizacion, CierreCajaJPA cierreCajaRef) {
        ContabilizacionARJPA entity = new ContabilizacionARJPA();
        entity.setId(contabilizacion.getId());
        entity.setCierreCaja(cierreCajaRef);
        entity.setFecha(contabilizacion.getFecha());
        entity.setConciliacionPOS(contabilizacion.isConciliacionPOS());
        entity.setAsientoProcesado(contabilizacion.isAsientoProcesado());
        entity.setAjusteDescuadre(contabilizacion.isAjusteDescuadre());
        entity.setRecetasAuditadas(contabilizacion.isRecetasAuditadas());
        entity.setRecetasCorrectas(contabilizacion.getRecetasCorrectas());
        entity.setMotivoObservacion(contabilizacion.getMotivoObservacion());
        entity.setSubsanacion(contabilizacion.isSubsanacion());
        entity.setConsolidado(contabilizacion.isConsolidado());
        entity.setEstado(contabilizacion.getEstado());
        return entity;
    }

    public static ContabilizacionAR ToDomain(ContabilizacionARJPA entity) {
        return ContabilizacionAR.reconstruir(
                entity.getId(),
                CierreCajaMapper.ToDomain(entity.getCierreCaja()),
                entity.getFecha(),
                entity.isConciliacionPOS(),
                entity.isAsientoProcesado(),
                entity.isAjusteDescuadre(),
                entity.isRecetasAuditadas(),
                entity.getRecetasCorrectas(),
                entity.getMotivoObservacion(),
                entity.isSubsanacion(),
                entity.isConsolidado(),
                entity.getEstado()
        );
    }
}
