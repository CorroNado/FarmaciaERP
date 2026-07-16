package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICuentaJPARepository extends JpaRepository<CuentaJPA, Long> {

    Optional<CuentaJPA> findByCodigo(String codigo);

    List<CuentaJPA> findByActivaTrue();

    boolean existsByCodigo(String codigo);
}