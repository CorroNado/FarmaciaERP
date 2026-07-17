package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.PlanillaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlanillaJPARepository extends JpaRepository<PlanillaJPA, Long> {

    Optional<PlanillaJPA> findByMesAndAnio(int mes, int anio);

    boolean existsByMesAndAnio(int mes, int anio);
}
