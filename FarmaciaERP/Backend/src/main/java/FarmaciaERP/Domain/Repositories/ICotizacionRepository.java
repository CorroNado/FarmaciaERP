package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Enums.EstadoCotizacion;

import java.util.List;
import java.util.Optional;

public interface ICotizacionRepository {

    Cotizacion save(Cotizacion cotizacion);

    Optional<Cotizacion> findById(Long id);

    List<Cotizacion> findAll();

    boolean existsById(Long id);

    List<Cotizacion> findByClienteId(Long clienteId);

    List<Cotizacion> findByEstado(EstadoCotizacion estado);
}
