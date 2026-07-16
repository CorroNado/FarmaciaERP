package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdenCompraJPARepository extends JpaRepository<OrdenCompraJPA, Long> {

    List<OrdenCompraJPA> findBySolicitudPedido_Id(Long solicitudPedidoId);

    List<OrdenCompraJPA> findByEstado(EstadoOrdenCompra estado);
}
