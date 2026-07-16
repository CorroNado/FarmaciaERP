package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.DispersionBancariaCierreJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDispersionBancariaCierreJPARepository extends JpaRepository<DispersionBancariaCierreJPA, Long> {

    boolean existsByPropuestaPagoAutomatica_Id(Long propuestaPagoAutomaticaId);
}
