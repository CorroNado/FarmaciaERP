package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Enums.EstadoAsiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IAsientoContableRepository {

    AsientoContable save(AsientoContable asientoContable);

    Optional<AsientoContable> findById(Long id);

    Optional<AsientoContable> findByNumero(String numero);

    List<AsientoContable> findAll();

    List<AsientoContable> findByEstado(EstadoAsiento estado);

    List<AsientoContable> findByFechaContableBetween(LocalDate inicio, LocalDate fin);

    boolean existsByNumero(String numero);
}