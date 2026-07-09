package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.enums.EmailLabel;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.domain.valueObjects.EmailAddress;

import java.util.List;
import java.util.Optional;

public interface IEmailContactRepository {

    void save(EmailContact saved);
    Optional<EmailContact> findById(Long id);

    List<EmailContact> findByOwnerId(Long ownerId, OwnerType ownerType);
    Optional<EmailContact> findPrincipalByOwnerId(Long ownerId, OwnerType ownerType);
    boolean existsByEmailAddress(EmailAddress email);

    void updateEmailDetails(Long id, EmailAddress newEmail, EmailLabel newLabel);
    void changePrincipal(Long ownerId, OwnerType ownerType, Long newPrincipalId);
}
