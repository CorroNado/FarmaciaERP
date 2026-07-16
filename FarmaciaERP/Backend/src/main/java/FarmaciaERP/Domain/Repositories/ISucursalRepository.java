package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Sucursal;

import java.util.List;
import java.util.Optional;

public interface ISucursalRepository {

    Sucursal save(Sucursal sucursal);

    Optional<Sucursal> findById(Long id);

    Optional<Sucursal> findByCodigo(String codigo);

    List<Sucursal> findAll();

    List<Sucursal> findAllActivas();

    boolean existsByCodigo(String codigo);
}
