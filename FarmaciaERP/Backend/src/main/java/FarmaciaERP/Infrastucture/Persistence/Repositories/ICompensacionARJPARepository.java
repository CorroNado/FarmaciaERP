package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CompensacionARJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompensacionARJPARepository extends JpaRepository<CompensacionARJPA, Long> {

    Optional<CompensacionARJPA> findByContabilizacionAR_Id(Long contabilizacionARId);
}
