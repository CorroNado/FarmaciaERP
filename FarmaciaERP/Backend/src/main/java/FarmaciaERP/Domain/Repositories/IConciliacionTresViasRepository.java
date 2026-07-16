package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.ConciliacionTresVias;

import java.util.List;
import java.util.Optional;

public interface IConciliacionTresViasRepository {

    ConciliacionTresVias save(ConciliacionTresVias conciliacionTresVias);

    Optional<ConciliacionTresVias> findById(Long id);

    List<ConciliacionTresVias> findAll();

    boolean existsById(Long id);

    List<ConciliacionTresVias> findByOrdenCompraId(Long ordenCompraId);

    boolean existsByOrdenCompraId(Long ordenCompraId);
}
