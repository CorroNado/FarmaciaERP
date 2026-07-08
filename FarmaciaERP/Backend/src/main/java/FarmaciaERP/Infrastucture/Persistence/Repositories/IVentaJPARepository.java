package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Infrastucture.Persistence.Entities.VentaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IVentaJPARepository extends JpaRepository<VentaJPA, Long> {

    List<VentaJPA> findByCliente_Id(Long clienteId);

    List<VentaJPA> findByEstado(EstadoVenta estado);

    List<VentaJPA> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
