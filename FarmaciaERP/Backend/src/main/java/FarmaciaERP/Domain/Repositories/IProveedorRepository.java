package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Domain.Enums.EstadoProveedor;

import java.util.List;
import java.util.Optional;

public interface IProveedorRepository {

    Proveedor save(Proveedor proveedor);

    Optional<Proveedor> findById(Long id);

    List<Proveedor> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<Proveedor> findByRuc(String ruc);

    List<Proveedor> findByRazonSocial(String razonSocial);

    List<Proveedor> findByEstado(EstadoProveedor estado);
}
