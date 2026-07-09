package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import FarmaciaERP.infrastucture.persistence.embeddable.EmailAddressEmb;
import FarmaciaERP.infrastucture.persistence.entities.EmailContactJPA;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class EmailContactMapper {

    public EmailContact toDomain(EmailContactJPA jpa) {
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

    public EmailContactJPA toJPA(EmailContact domain) {
        return new EmailContactJPA(
                domain.getOwnerId(),
                domain.getOwnerType(),
                emailAddresstoEmb(domain.getDireccion()),
                domain.getEtiqueta(),
                domain.getEstado(),
                domain.getFechaCreacion() != null ? domain.getFechaCreacion(): LocalDateTime.now()
        );
    }

    private EmailAddress emailAddresstoDomain(EmailAddressEmb emb) {
        return new EmailAddress(emb.getDireccion());
    }
    private EmailAddressEmb emailAddresstoEmb(EmailAddress domain) {
        return new EmailAddressEmb(domain.getDireccion());
    }
}