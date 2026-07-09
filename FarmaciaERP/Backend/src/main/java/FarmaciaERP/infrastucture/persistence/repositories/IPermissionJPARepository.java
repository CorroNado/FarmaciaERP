package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.PermissionJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPermissionJPARepository extends JpaRepository<PermissionJPA,Long> {
    Optional<PermissionJPA> findByCodigo(String codigo);
}
