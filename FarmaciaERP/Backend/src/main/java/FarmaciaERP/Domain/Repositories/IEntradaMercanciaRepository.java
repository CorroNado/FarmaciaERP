package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.EntradaMercancia;

import java.util.List;
import java.util.Optional;

public interface IEntradaMercanciaRepository {

    EntradaMercancia save(EntradaMercancia entradaMercancia);

    Optional<EntradaMercancia> findById(Long id);

    List<EntradaMercancia> findAll();

    boolean existsById(Long id);

    List<EntradaMercancia> findByOrdenCompraId(Long ordenCompraId);

    boolean existsByOrdenCompraId(Long ordenCompraId);
}
