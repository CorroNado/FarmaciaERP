package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.ExcepcionFacturacionJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExcepcionFacturacionJPARepository extends JpaRepository<ExcepcionFacturacionJPA, Long> {

    List<ExcepcionFacturacionJPA> findByConciliacionTresVias_Id(Long conciliacionTresViasId);

    boolean existsByConciliacionTresVias_Id(Long conciliacionTresViasId);
}
