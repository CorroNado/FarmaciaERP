package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.DevolucionJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDevolucionJPARepository extends JpaRepository<DevolucionJPA, Long> {

    List<DevolucionJPA> findByVenta_Id(Long ventaId);
}
