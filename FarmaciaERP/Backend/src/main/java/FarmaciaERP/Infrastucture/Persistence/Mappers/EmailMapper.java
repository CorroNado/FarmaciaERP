package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.DniEmb;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.EmailEmb;

public class EmailMapper {
    public static Email toDomain(EmailEmb embeddable){
        return embeddable == null ? null : new Email(embeddable.getEmail());
    }
    public static EmailEmb toEmbeddable(Email email){
        return email == null ? null : new EmailEmb(email.getEmail());
    }
}
