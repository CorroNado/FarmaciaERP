package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DebitoAR;
import FarmaciaERP.Infrastucture.Persistence.Entities.DebitoARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.RecetaMedicaARJPA;

public class DebitoARMapper {

    /**
     * recetaMedicaARRef debe ser una referencia YA GESTIONADA por JPA.
     */
    public static DebitoARJPA ToEntity(DebitoAR debito, RecetaMedicaARJPA recetaMedicaARRef) {
        DebitoARJPA entity = new DebitoARJPA();
        entity.setId(debito.getId());
        entity.setRecetaMedicaAR(recetaMedicaARRef);
        entity.setMonto(debito.getMonto());
        entity.setMotivo(debito.getMotivo());
        entity.setEstado(debito.getEstado());
        entity.setJustificado(debito.getJustificado());
        entity.setTramitado(debito.isTramitado());
        entity.setAjustado(debito.isAjustado());
        entity.setFecha(debito.getFecha());
        entity.setFechaAjuste(debito.getFechaAjuste());
        return entity;
    }

    public static DebitoAR ToDomain(DebitoARJPA entity) {
        return DebitoAR.reconstruir(
                entity.getId(),
                RecetaMedicaARMapper.ToDomain(entity.getRecetaMedicaAR()),
                entity.getMonto(),
                entity.getMotivo(),
                entity.getEstado(),
                entity.getJustificado(),
                entity.isTramitado(),
                entity.isAjustado(),
                entity.getFecha(),
                entity.getFechaAjuste()
        );
    }
}
