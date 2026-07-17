package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICentroCostoJPARepository extends JpaRepository<CentroCostoJPA, Long> {

    Optional<CentroCostoJPA> findByCodigo(String codigo);

    List<CentroCostoJPA> findByActivoTrue();

    boolean existsByCodigo(String codigo);
}