package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Enums.EstadoVenta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IVentaRepository {

    Venta save(Venta venta);

    Optional<Venta> findById(Long id);

    List<Venta> findAll();

    boolean existsById(Long id);

    List<Venta> findByClienteId(Long clienteId);

    List<Venta> findByEstado(EstadoVenta estado);

    List<Venta> findByFecha(LocalDate fecha);
}
