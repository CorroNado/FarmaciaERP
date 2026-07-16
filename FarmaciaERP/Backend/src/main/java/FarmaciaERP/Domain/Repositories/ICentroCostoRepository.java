package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.CentroCosto;

import java.util.List;
import java.util.Optional;

public interface ICentroCostoRepository {

    CentroCosto save(CentroCosto centroCosto);

    Optional<CentroCosto> findById(Long id);

    Optional<CentroCosto> findByCodigo(String codigo);

    List<CentroCosto> findAll();

    List<CentroCosto> findAllActivos();

    boolean existsByCodigo(String codigo);
}