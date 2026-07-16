package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEntradaMercanciaJPARepository extends JpaRepository<EntradaMercanciaJPA, Long> {

    List<EntradaMercanciaJPA> findByOrdenCompra_Id(Long ordenCompraId);

    boolean existsByOrdenCompra_Id(Long ordenCompraId);
}
