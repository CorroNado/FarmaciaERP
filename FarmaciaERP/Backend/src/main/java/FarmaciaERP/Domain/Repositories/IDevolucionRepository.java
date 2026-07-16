package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Devolucion;

import java.util.List;
import java.util.Optional;

public interface IDevolucionRepository {

    Devolucion save(Devolucion devolucion);

    Optional<Devolucion> findById(Long id);

    List<Devolucion> findAll();

    List<Devolucion> findByVentaId(Long ventaId);
}
