package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.RecetaMedicaARJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecetaMedicaARJPARepository extends JpaRepository<RecetaMedicaARJPA, Long> {

    List<RecetaMedicaARJPA> findByContabilizacionAR_Id(Long contabilizacionARId);
}
