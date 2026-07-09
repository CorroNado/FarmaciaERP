package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.domain.repositories.IClienteRepository;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.infrastucture.persistence.entities.ClienteJPA;
import FarmaciaERP.infrastucture.persistence.mappers.ClienteMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IClienteJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements IClienteRepository {

    private final IClienteJPARepository jpaRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public Cliente save(Cliente cliente) {
        ClienteJPA entity = clienteMapper.toEntity(cliente);
        ClienteJPA saved = jpaRepository.save(entity);
        return clienteMapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id)
                .map(clienteMapper::toDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(clienteMapper::toDomain)
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
    public Optional<Cliente> findByDni(Dni documentoIdentidad) {
        return jpaRepository.findByDni_Dni(documentoIdentidad.getValor())
                .map(clienteMapper::toDomain);
    }

    @Override
    public List<Cliente> findByName(FullName nombres) {
        return jpaRepository.findByNombres(clienteMapper.fullNameToEmb(nombres))
                .stream()
                .map(clienteMapper::toDomain)
                .toList();
    }

    @Override
    public List<Cliente> findByInsurance(InsuranceType InsuranceType) {
        return jpaRepository.findByInsuranceType(InsuranceType)
                .stream()
                .map(clienteMapper::toDomain)
                .toList();
    }

}
