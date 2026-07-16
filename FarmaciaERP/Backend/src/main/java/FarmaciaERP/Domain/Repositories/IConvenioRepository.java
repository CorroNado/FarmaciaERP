package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Convenio;
import FarmaciaERP.Domain.Enums.EstadoConvenio;

import java.util.List;
import java.util.Optional;

public interface IConvenioRepository {

    Convenio save(Convenio convenio);

    Optional<Convenio> findById(Long id);

    List<Convenio> findAll();

    boolean existsById(Long id);

    Optional<Convenio> findByNumero(String numero);

    List<Convenio> findByProveedorId(Long proveedorId);

    List<Convenio> findByEstado(EstadoConvenio estado);
}
