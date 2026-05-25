package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Customer;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.domain.repositories.IClienteRepository;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.infrastucture.persistence.entities.CustomerJPA;
import FarmaciaERP.infrastucture.persistence.mappers.CustomerMapper;
import FarmaciaERP.infrastucture.persistence.repositories.ICustomerJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryImpl implements IClienteRepository {

    private final ICustomerJPARepository jpaRepository;

    public ClienteRepositoryImpl(ICustomerJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJPA entity = CustomerMapper.ToEntity(customer);
        CustomerJPA saved = jpaRepository.save(entity);
        return CustomerMapper.ToDomain(saved);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CustomerMapper::ToDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(CustomerMapper::ToDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Customer> findByDni(Dni documentoIdentidad) {
        return jpaRepository.findByDni_Dni(documentoIdentidad.getValor())
                .map(CustomerMapper::ToDomain);
    }

    @Override
    public List<Customer> findByName(FullName nombres) {
        return jpaRepository.findByNombres(FullnameMapper.toEmbeddable(nombres))
                .stream()
                .map(CustomerMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Customer> findByInsurance(InsuranceType insuranceType) {
        return jpaRepository.findByTipoSeguro(insuranceType)
                .stream()
                .map(CustomerMapper::ToDomain)
                .toList();
    }



}
