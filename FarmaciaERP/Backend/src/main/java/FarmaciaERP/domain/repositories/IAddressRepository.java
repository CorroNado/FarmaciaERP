package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Address;
import FarmaciaERP.domain.enums.AddressLabel;
import FarmaciaERP.domain.enums.OwnerType;

import java.util.List;
import java.util.Optional;

public interface IAddressRepository {
    void save(Address saved);
    Optional<Address> findById(Long id);

    List<Address> findByOwnerId(Long ownerId, OwnerType ownerType);
    Optional<Address> findPrincipalByOwnerId(Long ownerId, OwnerType ownerType);
    boolean existsById(Long id);

    void updateDetails(Long id, String newDescription, AddressLabel newLabel);
    void changePrincipal(Long ownerId, OwnerType ownerType, Long newPrincipalId);
}
