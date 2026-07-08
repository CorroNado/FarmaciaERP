package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;

public class MedicamentoMapper {

    public static MedicamentoJPA ToEntity(Medicamento medicamento) {
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

    public static Medicamento ToDomain(MedicamentoJPA entity) {
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
