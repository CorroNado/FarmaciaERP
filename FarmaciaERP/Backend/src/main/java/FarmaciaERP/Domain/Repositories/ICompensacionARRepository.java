package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.CompensacionAR;

import java.util.List;
import java.util.Optional;

public interface ICompensacionARRepository {

    CompensacionAR save(CompensacionAR compensacion);

    Optional<CompensacionAR> findById(Long id);

    Optional<CompensacionAR> findByContabilizacionARId(Long contabilizacionARId);

    List<CompensacionAR> findAll();
}
