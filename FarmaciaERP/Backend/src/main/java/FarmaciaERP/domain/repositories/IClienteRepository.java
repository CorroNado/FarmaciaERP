package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;

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

    List<Cliente> findByInsurance(InsuranceType InsuranceType);
}
