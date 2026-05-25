package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.enums.EmailLabel;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.domain.valueObjects.EmailAddress;

import java.util.List;
import java.util.Optional;

public interface IEmailContactRepository {
    EmailContact save(EmailContact email);
    void updateEmailAddress(Long id, EmailAddress newEmail);
    void updateLabel(Long id, EmailLabel newLabel);

    Optional<EmailContact> findById(Long id);
    List<EmailContact> findByOwnerId(Long ownerId, OwnerType ownerType);
    Optional<EmailContact> findPrincipalByOwnerId(Long ownerId, OwnerType ownerType);
    boolean existsByEmailAddress(EmailAddress email, OwnerType ownerType);

    void changeStatus(Long id, EmailStatus status);
    void changePrincipal(Long currentPrincipalId, Long newPrincipalId);
}
