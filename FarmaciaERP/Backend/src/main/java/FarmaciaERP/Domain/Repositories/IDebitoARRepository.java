package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.DebitoAR;

import java.util.List;
import java.util.Optional;

public interface IDebitoARRepository {

    DebitoAR save(DebitoAR debito);

    Optional<DebitoAR> findById(Long id);

    Optional<DebitoAR> findByRecetaMedicaARId(Long recetaMedicaARId);

    List<DebitoAR> findByContabilizacionARId(Long contabilizacionARId);

    List<DebitoAR> findAll();
}
