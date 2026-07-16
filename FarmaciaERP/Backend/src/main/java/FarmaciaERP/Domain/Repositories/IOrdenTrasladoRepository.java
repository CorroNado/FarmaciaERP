package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.OrdenTraslado;

import java.util.List;
import java.util.Optional;

public interface IOrdenTrasladoRepository {

    OrdenTraslado save(OrdenTraslado ordenTraslado);

    Optional<OrdenTraslado> findById(Long id);

    List<OrdenTraslado> findAll();

    List<OrdenTraslado> findByInspeccionCalidadId(Long inspeccionCalidadId);

    List<OrdenTraslado> findBySucursalDestinoId(Long sucursalId);

    boolean existsByInspeccionCalidadId(Long inspeccionCalidadId);
}
