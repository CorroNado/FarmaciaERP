package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISucursalJPARepository extends JpaRepository<SucursalJPA, Long> {

    Optional<SucursalJPA> findByCodigo(String codigo);

    List<SucursalJPA> findByActivaTrue();

    boolean existsByCodigo(String codigo);
}
