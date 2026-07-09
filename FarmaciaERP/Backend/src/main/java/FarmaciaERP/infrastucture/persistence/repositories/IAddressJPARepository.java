package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.domain.entities.Address;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.infrastucture.persistence.entities.AddressJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAddressJPARepository extends JpaRepository<AddressJPA,Long> {
    @Query("SELECT a FROM AddressJPA a WHERE a.dueñoId = :ownerId AND a.tipoDueño = :ownerType")
    List<AddressJPA> findByOwnerId(Long ownerId, OwnerType ownerType);
}
