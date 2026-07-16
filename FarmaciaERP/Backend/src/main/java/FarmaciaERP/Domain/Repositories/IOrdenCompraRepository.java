package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;

import java.util.List;
import java.util.Optional;

public interface IOrdenCompraRepository {

    OrdenCompra save(OrdenCompra ordenCompra);

    Optional<OrdenCompra> findById(Long id);

    List<OrdenCompra> findAll();

    boolean existsById(Long id);

    List<OrdenCompra> findBySolicitudPedidoId(Long solicitudPedidoId);

    List<OrdenCompra> findByEstado(EstadoOrdenCompra estado);
}
