package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.DisputaComercialJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDisputaComercialJPARepository extends JpaRepository<DisputaComercialJPA, Long> {

    List<DisputaComercialJPA> findByExcepcionFacturacion_Id(Long excepcionFacturacionId);

    boolean existsByExcepcionFacturacion_Id(Long excepcionFacturacionId);
}
