package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.ItemConvenio;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConvenioJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ItemConvenioJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;

public class ItemConvenioMapper {

    public static ItemConvenioJPA ToEntity(ItemConvenio item, ConvenioJPA convenioRef, MedicamentoJPA medicamentoRef) {
        ItemConvenioJPA entity = new ItemConvenioJPA();
        entity.setId(item.getId());
        entity.setConvenio(convenioRef);
        entity.setMedicamento(medicamentoRef);
        entity.setPrecioPactado(item.getPrecioPactado());
        return entity;
    }

    public static ItemConvenio ToDomain(ItemConvenioJPA entity) {
        return new ItemConvenio(MedicamentoMapper.ToDomain(entity.getMedicamento()), entity.getPrecioPactado());
    }
}
