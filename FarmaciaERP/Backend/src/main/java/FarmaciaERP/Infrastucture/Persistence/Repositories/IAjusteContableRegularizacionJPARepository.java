package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.AjusteContableRegularizacionJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAjusteContableRegularizacionJPARepository extends JpaRepository<AjusteContableRegularizacionJPA, Long> {

    List<AjusteContableRegularizacionJPA> findByDisputaComercial_Id(Long disputaComercialId);

    boolean existsByDisputaComercial_Id(Long disputaComercialId);
}
