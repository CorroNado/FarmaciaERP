package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISubcuentaDivisionariaJPARepository extends JpaRepository<SubcuentaDivisionariaJPA, Long> {

    Optional<SubcuentaDivisionariaJPA> findByCodigo(String codigo);

    List<SubcuentaDivisionariaJPA> findByCuenta_Id(Long cuentaId);

    boolean existsByCodigo(String codigo);
}