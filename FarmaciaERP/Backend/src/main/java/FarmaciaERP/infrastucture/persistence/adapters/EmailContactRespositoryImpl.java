package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.enums.EmailLabel;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.domain.repositories.IEmailContactRepository;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import FarmaciaERP.infrastucture.persistence.embeddable.EmailAddressEmb;
import FarmaciaERP.infrastucture.persistence.entities.EmailContactJPA;
import FarmaciaERP.infrastucture.persistence.mappers.EmailContactMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IEmailJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailContactRespositoryImpl implements IEmailContactRepository {
    private final IEmailJPARepository jpaRepository;
    private final EmailContactMapper emailContactMapper;

    public void save(EmailContact saved){
        EmailContactJPA jpa = emailContactMapper.toJPA(saved);
        if(saved.getId() != null){
            jpaRepository.findById(saved.getId()).ifPresent(
                entityPersistence -> {
                    entityPersistence.setDireccion(jpa.getDireccion());
                    entityPersistence.setEtiqueta(jpa.getEtiqueta());
                    entityPersistence.setEstado(jpa.getEstado());
                    jpaRepository.save(entityPersistence);
                });
        }else {
            jpaRepository.save(jpa);
        }

    }
    public Optional<EmailContact> findById(Long id){
        return jpaRepository.findById(id)
                .map(emailContactMapper::toDomain);
    }
    public List<EmailContact> findByOwnerId(Long ownerId, OwnerType ownerType){
        return jpaRepository.findByOwnerId(ownerId,ownerType)
                .stream()
                .map(emailContactMapper::toDomain)
                .toList();
    }
    public Optional<EmailContact> findPrincipalByOwnerId(Long ownerId, OwnerType ownerType){
        return jpaRepository.findByOwnerId(ownerId,ownerType)
                .stream()
                .filter(e -> e.getEstado() == EmailStatus.PRINCIPAL)
                .map(emailContactMapper::toDomain)
                .findFirst();
    }
    public boolean existsByEmailAddress(EmailAddress email){
        return jpaRepository.existsByEmailAddress(email.getDireccion());
    }
    public void updateEmailDetails(Long id, EmailAddress newEmail, EmailLabel newLabel){
        EmailContactJPA email = jpaRepository.findById(id).orElse(null);
        if(email == null) return;
        email.setDireccion(new EmailAddressEmb(newEmail.getDireccion()));
        email.setEtiqueta(newLabel);
        jpaRepository.save(email);
    }
    public void changePrincipal(Long ownerId, OwnerType ownerType, Long newPrincipalId){
        List<EmailContactJPA> emails = jpaRepository.findByOwnerId(ownerId,ownerType);
        if(emails.isEmpty()) return;
        EmailContactJPA newPrincipal = emails.stream()
                .filter(e -> e.getId().equals(newPrincipalId))
                .findFirst().orElse(null);
        if(newPrincipal == null) return;
        newPrincipal.setEstado(EmailStatus.PRINCIPAL);
        jpaRepository.save(newPrincipal);
    }

}
