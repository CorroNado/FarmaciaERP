package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.domain.entities.Profile;
import FarmaciaERP.infrastucture.persistence.entities.ProfileJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfileJPARepository extends JpaRepository<ProfileJPA,Long> {
    Optional<ProfileJPA> findByNombre(String nombre);
}
