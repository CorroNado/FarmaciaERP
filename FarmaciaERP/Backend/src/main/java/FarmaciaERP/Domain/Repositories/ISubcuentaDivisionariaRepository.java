package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;

import java.util.List;
import java.util.Optional;

public interface ISubcuentaDivisionariaRepository {

    SubcuentaDivisionaria save(SubcuentaDivisionaria subcuenta);

    Optional<SubcuentaDivisionaria> findById(Long id);

    Optional<SubcuentaDivisionaria> findByCodigo(String codigo);

    List<SubcuentaDivisionaria> findAll();

    List<SubcuentaDivisionaria> findByCuentaId(Long cuentaId);

    boolean existsByCodigo(String codigo);
}