package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.DisputaComercial;

import java.util.List;
import java.util.Optional;

public interface IDisputaComercialRepository {

    DisputaComercial save(DisputaComercial disputaComercial);

    Optional<DisputaComercial> findById(Long id);

    List<DisputaComercial> findAll();

    boolean existsById(Long id);

    List<DisputaComercial> findByExcepcionFacturacionId(Long excepcionFacturacionId);

    boolean existsByExcepcionFacturacionId(Long excepcionFacturacionId);
}
