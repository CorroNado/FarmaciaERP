package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;

import java.util.List;
import java.util.Optional;

public interface IAjusteContableRegularizacionRepository {

    AjusteContableRegularizacion save(AjusteContableRegularizacion ajusteContableRegularizacion);

    Optional<AjusteContableRegularizacion> findById(Long id);

    List<AjusteContableRegularizacion> findAll();

    boolean existsById(Long id);

    List<AjusteContableRegularizacion> findByDisputaComercialId(Long disputaComercialId);

    boolean existsByDisputaComercialId(Long disputaComercialId);
}
