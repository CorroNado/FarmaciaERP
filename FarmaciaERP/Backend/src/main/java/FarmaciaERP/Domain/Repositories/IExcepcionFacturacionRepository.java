package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;

import java.util.List;
import java.util.Optional;

public interface IExcepcionFacturacionRepository {

    ExcepcionFacturacion save(ExcepcionFacturacion excepcionFacturacion);

    Optional<ExcepcionFacturacion> findById(Long id);

    List<ExcepcionFacturacion> findAll();

    boolean existsById(Long id);

    List<ExcepcionFacturacion> findByConciliacionTresViasId(Long conciliacionTresViasId);

    boolean existsByConciliacionTresViasId(Long conciliacionTresViasId);
}
