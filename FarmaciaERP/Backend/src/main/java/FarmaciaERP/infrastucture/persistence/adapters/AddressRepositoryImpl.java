package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Address;
import FarmaciaERP.domain.enums.AddressLabel;
import FarmaciaERP.domain.enums.AddressStatus;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.domain.repositories.IAddressRepository;
import FarmaciaERP.infrastucture.persistence.embeddable.EmailAddressEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.PasswordEmb;
import FarmaciaERP.infrastucture.persistence.entities.AddressJPA;
import FarmaciaERP.infrastucture.persistence.entities.EmailContactJPA;
import FarmaciaERP.infrastucture.persistence.entities.UserJPA;
import FarmaciaERP.infrastucture.persistence.mappers.AddressMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IAddressJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements IAddressRepository {
    private final IAddressJPARepository jpaRepository;
    private final AddressMapper addressMapper;

    @Override
    public void save(Address saved) {
        AddressJPA jpa = addressMapper.toJPA(saved);
        if (saved.getId() != null) {
            jpaRepository.findById(saved.getId()).ifPresent(entityPersistente -> {
                entityPersistente.setDescripcion(jpa.getDescripcion());
                entityPersistente.setEstado(jpa.getEstado());
                entityPersistente.setEtiqueta(jpa.getEtiqueta());
                entityPersistente.setDistrito(jpa.getDistrito());
                jpaRepository.save(entityPersistente);
            });
        } else {
            jpaRepository.save(jpa);
        }
    }

    @Override
    public Optional<Address> findById(Long id) {
        return jpaRepository.findById(id)
                .map(addressMapper::toDomain);
    }

    @Override
    public List<Address> findByOwnerId(Long ownerId, OwnerType ownerType) {
        return jpaRepository.findByOwnerId(ownerId, ownerType)
                .stream()
                .map(addressMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Address> findPrincipalByOwnerId(Long ownerId, OwnerType ownerType) {
        return jpaRepository.findByOwnerId(ownerId, ownerType)
                .stream()
                .filter(a-> a.getEstado() == AddressStatus.PRINCIPAL)
                .map(addressMapper::toDomain).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void updateDetails(Long id, String newDescription, AddressLabel newLabel) {
        AddressJPA direccion = jpaRepository.findById(id).orElse(null);
        if(direccion == null) return;
        direccion.setDescripcion(newDescription);
        direccion.setEtiqueta(newLabel);
        jpaRepository.save(direccion);
    }

    @Override
    public void changePrincipal(Long ownerId, OwnerType ownerType, Long newPrincipalId) {
        List<AddressJPA> direcciones = jpaRepository.findByOwnerId(ownerId,ownerType);
        if(direcciones.isEmpty()) return;
        AddressJPA newPrincipal = direcciones.stream()
                .filter(a -> a.getId().equals(newPrincipalId))
                .findFirst().orElse(null);
        if(newPrincipal == null) return;
        newPrincipal.setEstado(AddressStatus.PRINCIPAL);
        jpaRepository.save(newPrincipal);
    }
}
