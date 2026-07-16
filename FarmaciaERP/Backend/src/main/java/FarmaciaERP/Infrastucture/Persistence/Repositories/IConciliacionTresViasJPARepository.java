package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConciliacionTresViasJPARepository extends JpaRepository<ConciliacionTresViasJPA, Long> {

    List<ConciliacionTresViasJPA> findByOrdenCompra_Id(Long ordenCompraId);

    boolean existsByOrdenCompra_Id(Long ordenCompraId);
}
