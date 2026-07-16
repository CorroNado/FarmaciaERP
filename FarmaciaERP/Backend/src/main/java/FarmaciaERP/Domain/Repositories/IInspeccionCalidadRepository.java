package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.InspeccionCalidad;

import java.util.List;
import java.util.Optional;

public interface IInspeccionCalidadRepository {

    InspeccionCalidad save(InspeccionCalidad inspeccionCalidad);

    Optional<InspeccionCalidad> findById(Long id);

    List<InspeccionCalidad> findAll();

    boolean existsById(Long id);

    List<InspeccionCalidad> findByEntradaMercanciaId(Long entradaMercanciaId);

    boolean existsByEntradaMercanciaId(Long entradaMercanciaId);
}
