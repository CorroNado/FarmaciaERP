package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Cuenta;

import java.util.List;
import java.util.Optional;

public interface ICuentaRepository {

    Cuenta save(Cuenta cuenta);

    Optional<Cuenta> findById(Long id);

    Optional<Cuenta> findByCodigo(String codigo);

    List<Cuenta> findAll();

    List<Cuenta> findAllActivas();

    boolean existsByCodigo(String codigo);
}