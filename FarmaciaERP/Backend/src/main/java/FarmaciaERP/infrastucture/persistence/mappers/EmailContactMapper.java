package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import FarmaciaERP.infrastucture.persistence.embeddable.EmailAddressEmb;
import FarmaciaERP.infrastucture.persistence.entities.EmailContactJPA;

import java.time.LocalDateTime;

public class EmailContactMapper {

    public static EmailContact toDomain(EmailContactJPA jpa) {
        return new EmailContact(
                jpa.getId(),
                jpa.getOwnerId(),
                jpa.getOwnerType(),
                emailAddresstoDomain(jpa.getDireccion()),
                jpa.getEtiqueta(),
                jpa.getEstado(),
                jpa.getFechaCreacion()
        );
    }

    public static EmailContactJPA toJPA(EmailContact domain) {
        return new EmailContactJPA(
                domain.getOwnerId(),
                domain.getOwnerType(),
                emailAddresstoEmb(domain.getDireccion()),
                domain.getEtiqueta(),
                domain.getStatus(),
                domain.getCreatedAt() != null ? domain.getCreatedAt() : LocalDateTime.now()
        );
    }

    private static EmailAddress emailAddresstoDomain(EmailAddressEmb jpa) {
        return new EmailAddress(jpa.getAddress());
    }
    private static EmailAddressEmb emailAddresstoEmb(EmailAddress domain) {
        return new EmailAddressEmb(domain.getDireccion());
    }
}