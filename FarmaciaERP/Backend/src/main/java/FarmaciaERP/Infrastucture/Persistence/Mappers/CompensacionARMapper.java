package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.CompensacionAR;
import FarmaciaERP.Infrastucture.Persistence.Entities.CompensacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;

public class CompensacionARMapper {

    /**
     * contabilizacionARRef debe ser una referencia YA GESTIONADA por JPA.
     */
    public static CompensacionARJPA ToEntity(CompensacionAR compensacion, ContabilizacionARJPA contabilizacionARRef) {
        CompensacionARJPA entity = new CompensacionARJPA();
        entity.setId(compensacion.getId());
        entity.setContabilizacionAR(contabilizacionARRef);
        entity.setCompensado(compensacion.isCompensado());
        entity.setReporteGenerado(compensacion.isReporteGenerado());
        entity.setMontoVentas(compensacion.getMontoVentas());
        entity.setMontoAprobadas(compensacion.getMontoAprobadas());
        entity.setPerdidas(compensacion.getPerdidas());
        entity.setMargenNeto(compensacion.getMargenNeto());
        entity.setMargenPct(compensacion.getMargenPct());
        entity.setSaldoConfirmado(compensacion.isSaldoConfirmado());
        entity.setCerrado(compensacion.isCerrado());
        entity.setEstado(compensacion.getEstado());
        entity.setFecha(compensacion.getFecha());
        entity.setFechaCierre(compensacion.getFechaCierre());
        return entity;
    }

    public static CompensacionAR ToDomain(CompensacionARJPA entity) {
        return CompensacionAR.reconstruir(
                entity.getId(),
                ContabilizacionARMapper.ToDomain(entity.getContabilizacionAR()),
                entity.isCompensado(),
                entity.isReporteGenerado(),
                entity.getMontoVentas(),
                entity.getMontoAprobadas(),
                entity.getPerdidas(),
                entity.getMargenNeto(),
                entity.getMargenPct(),
                entity.isSaldoConfirmado(),
                entity.isCerrado(),
                entity.getEstado(),
                entity.getFecha(),
                entity.getFechaCierre()
        );
    }
}
