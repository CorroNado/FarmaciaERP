package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Address;
import FarmaciaERP.infrastucture.persistence.entities.AddressJPA;

public class AddressMapper {

    public static Address toDomain(AddressJPA jpa) {
        return new Address(
                jpa.getId(),
                jpa.getUsuario().getId(),
                jpa.getDescripcion(),
                jpa.getEtiqueta(),
                jpa.getEstado(),
                jpa.getDistrito().getId()
        );
    }

    public static AddressJPA toJPA(Address domain) {
        return new AddressJPA(
                null,
                null,
                domain.getDescription(),
                domain.getLabel(),
                domain.getStatus()
        );
    }
}