package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;

import java.util.List;
import java.util.Optional;

public interface IDispersionBancariaCierreRepository {

    DispersionBancariaCierre save(DispersionBancariaCierre dispersionBancariaCierre);

    Optional<DispersionBancariaCierre> findById(Long id);

    List<DispersionBancariaCierre> findAll();

    boolean existsById(Long id);

    boolean existsByPropuestaPagoAutomaticaId(Long propuestaPagoAutomaticaId);
}
