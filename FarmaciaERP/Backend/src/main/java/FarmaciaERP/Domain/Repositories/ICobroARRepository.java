package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.CobroAR;

import java.util.List;
import java.util.Optional;

public interface ICobroARRepository {

    CobroAR save(CobroAR cobro);

    Optional<CobroAR> findById(Long id);

    Optional<CobroAR> findByContabilizacionARId(Long contabilizacionARId);

    List<CobroAR> findAll();
}
