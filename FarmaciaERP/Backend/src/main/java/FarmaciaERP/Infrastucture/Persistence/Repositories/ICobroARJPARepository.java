package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CobroARJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICobroARJPARepository extends JpaRepository<CobroARJPA, Long> {

    Optional<CobroARJPA> findByContabilizacionAR_Id(Long contabilizacionARId);
}
