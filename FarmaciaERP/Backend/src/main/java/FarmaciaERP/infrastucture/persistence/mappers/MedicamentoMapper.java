package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Medicamento;
import FarmaciaERP.infrastucture.persistence.entities.MedicamentoJPA;
import org.springframework.stereotype.Component;

@Component
public class MedicamentoMapper {

    public MedicamentoJPA toEntity(Medicamento medicamento) {
        MedicamentoJPA entity = new MedicamentoJPA();
        if (medicamento.getId() != 0) {
            entity.setId(medicamento.getId());
        }
        entity.setNombre(medicamento.getNombre());
        entity.setPresentacion(medicamento.getPresentacion());
        entity.setPrecio(medicamento.getPrecio());
        entity.setStock(medicamento.getStock());
        entity.setCategoria(medicamento.getCategoria());
        entity.setFechaVencimiento(medicamento.getFechaVencimiento());
        return entity;
    }

    public Medicamento toDomain(MedicamentoJPA entity) {
        return new Medicamento(
                entity.getId(),
                entity.getNombre(),
                entity.getPresentacion(),
                entity.getPrecio(),
                entity.getStock(),
                entity.getCategoria(),
                entity.getFechaVencimiento()
        );
    }
}
