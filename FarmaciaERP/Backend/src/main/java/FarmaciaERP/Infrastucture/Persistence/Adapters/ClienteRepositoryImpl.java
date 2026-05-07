package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ClienteMapper;
import FarmaciaERP.Infrastucture.Persistence.Mappers.FullnameMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IClienteJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryImpl implements IClienteRepository {

    private final IClienteJPARepository jpaRepository;

    public ClienteRepositoryImpl(IClienteJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteJPA entity = ClienteMapper.ToEntity(cliente);
        ClienteJPA saved = jpaRepository.save(entity);
        return ClienteMapper.ToDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ClienteMapper::ToDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ClienteMapper::ToDomain)
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
        return jpaRepository.findByDni_Dni(documentoIdentidad.getDni())
                .map(ClienteMapper::ToDomain);
    }

    @Override
    public List<Cliente> findByName(FullName nombres) {
        return jpaRepository.findByNombres(FullnameMapper.toEmbeddable(nombres))
                .stream()
                .map(ClienteMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Cliente> findByInsurance(TipoSeguro tipoSeguro) {
        return jpaRepository.findByTipoSeguro(tipoSeguro)
                .stream()
                .map(ClienteMapper::ToDomain)
                .toList();
    }



}
