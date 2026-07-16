package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.DebitoARJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDebitoARJPARepository extends JpaRepository<DebitoARJPA, Long> {

    Optional<DebitoARJPA> findByRecetaMedicaAR_Id(Long recetaMedicaARId);

    @Query("SELECT d FROM DebitoARJPA d WHERE d.recetaMedicaAR.contabilizacionAR.id = :contabilizacionARId")
    List<DebitoARJPA> findByContabilizacionARId(@Param("contabilizacionARId") Long contabilizacionARId);
}
