package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFacturaMIROJPARepository extends JpaRepository<FacturaMIROJPA, Long> {

    List<FacturaMIROJPA> findByOrdenCompra_Id(Long ordenCompraId);

    boolean existsByOrdenCompra_Id(Long ordenCompraId);

    Optional<FacturaMIROJPA> findByNumeroFactura(String numeroFactura);
}
