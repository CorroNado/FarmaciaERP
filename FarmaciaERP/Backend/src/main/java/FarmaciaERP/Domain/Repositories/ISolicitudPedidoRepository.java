package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Domain.Enums.EstadoSolPed;

import java.util.List;
import java.util.Optional;

public interface ISolicitudPedidoRepository {

    SolicitudPedido save(SolicitudPedido solicitudPedido);

    Optional<SolicitudPedido> findById(Long id);

    List<SolicitudPedido> findAll();

    boolean existsById(Long id);

    List<SolicitudPedido> findByEstado(EstadoSolPed estado);
}
