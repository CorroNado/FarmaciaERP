package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;

import java.util.List;
import java.util.Optional;
public interface IClienteRepository {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    List<Cliente> findAll();

    void deleteById(Long id);

    boolean existById(Long id);

    Optional<Cliente> findByDni(Dni documentoIdentidad);

    List<Cliente> findByName(FullName nombre);

    List<Cliente> findByInsurance(TipoSeguro tipoSeguro);
}