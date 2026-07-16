package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoCotizacion;
import FarmaciaERP.Infrastucture.Persistence.Entities.CotizacionJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICotizacionJPARepository extends JpaRepository<CotizacionJPA, Long> {

    List<CotizacionJPA> findByCliente_Id(Long clienteId);

    List<CotizacionJPA> findByEstado(EstadoCotizacion estado);
}
