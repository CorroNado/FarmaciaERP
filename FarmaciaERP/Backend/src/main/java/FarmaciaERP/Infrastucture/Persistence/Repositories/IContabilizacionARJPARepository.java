package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IContabilizacionARJPARepository extends JpaRepository<ContabilizacionARJPA, Long> {

    Optional<ContabilizacionARJPA> findByCierreCaja_Id(Long cierreCajaId);
}
