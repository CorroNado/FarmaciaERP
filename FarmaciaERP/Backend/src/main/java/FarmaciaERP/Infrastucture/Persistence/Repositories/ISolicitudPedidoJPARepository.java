package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoSolPed;
import FarmaciaERP.Infrastucture.Persistence.Entities.SolicitudPedidoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudPedidoJPARepository extends JpaRepository<SolicitudPedidoJPA, Long> {

    List<SolicitudPedidoJPA> findByEstado(EstadoSolPed estado);
}
