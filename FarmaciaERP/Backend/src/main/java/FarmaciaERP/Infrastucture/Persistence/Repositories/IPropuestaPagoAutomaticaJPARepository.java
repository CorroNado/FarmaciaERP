package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.PropuestaPagoAutomaticaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPropuestaPagoAutomaticaJPARepository extends JpaRepository<PropuestaPagoAutomaticaJPA, Long> {

    boolean existsByLotePagoTesoreria_Id(Long lotePagoTesoreriaId);
}
