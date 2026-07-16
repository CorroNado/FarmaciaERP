package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.CobroAR;
import FarmaciaERP.Infrastucture.Persistence.Entities.CobroARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;

public class CobroARMapper {

    /**
     * contabilizacionARRef debe ser una referencia YA GESTIONADA por JPA.
     */
    public static CobroARJPA ToEntity(CobroAR cobro, ContabilizacionARJPA contabilizacionARRef) {
        CobroARJPA entity = new CobroARJPA();
        entity.setId(cobro.getId());
        entity.setContabilizacionAR(contabilizacionARRef);
        entity.setMontoTransferido(cobro.getMontoTransferido());
        entity.setRetenciones(cobro.getRetenciones());
        entity.setComisionPct(cobro.getComisionPct());
        entity.setMontoConciliado(cobro.getMontoConciliado());
        entity.setDiferencia(cobro.getDiferencia());
        entity.setCuadra(cobro.getCuadra());
        entity.setRegistrado(cobro.isRegistrado());
        entity.setEstado(cobro.getEstado());
        entity.setFecha(cobro.getFecha());
        entity.setFechaRegistro(cobro.getFechaRegistro());
        return entity;
    }

    public static CobroAR ToDomain(CobroARJPA entity) {
        return CobroAR.reconstruir(
                entity.getId(),
                ContabilizacionARMapper.ToDomain(entity.getContabilizacionAR()),
                entity.getMontoTransferido(),
                entity.getRetenciones(),
                entity.getComisionPct(),
                entity.getMontoConciliado(),
                entity.getDiferencia(),
                entity.getCuadra(),
                entity.isRegistrado(),
                entity.getEstado(),
                entity.getFecha(),
                entity.getFechaRegistro()
        );
    }
}
