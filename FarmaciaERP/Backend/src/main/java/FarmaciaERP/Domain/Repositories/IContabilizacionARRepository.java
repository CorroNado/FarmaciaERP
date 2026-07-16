package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.ContabilizacionAR;

import java.util.List;
import java.util.Optional;

public interface IContabilizacionARRepository {

    ContabilizacionAR save(ContabilizacionAR contabilizacion);

    Optional<ContabilizacionAR> findById(Long id);

    Optional<ContabilizacionAR> findByCierreCajaId(Long cierreCajaId);

    List<ContabilizacionAR> findAll();
}
