package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Address;
import FarmaciaERP.infrastucture.persistence.entities.AddressJPA;
import FarmaciaERP.infrastucture.persistence.entities.DistrictJPA;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toDomain(AddressJPA jpa) {
        return new Address(
                jpa.getId(),
                jpa.getDueñoId(),
                jpa.getTipoDueño(),
                jpa.getDescripcion(),
                jpa.getEtiqueta(),
                jpa.getEstado(),
                jpa.getDistrito().getDistritoId()
        );
    }

    public AddressJPA toJPA(Address domain) {
        return new AddressJPA(
                domain.getId(),
                domain.getDueñoId(),
                domain.getTipoDueño(),
                new DistrictJPA(domain.getDistrictId()),
                domain.getDescripcion() ,
                domain.getEtiqueta(),
                domain.getEstado()
        );
    }
}