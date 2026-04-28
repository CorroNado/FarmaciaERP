package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ClienteMapper;
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
    public Cliente guardar(Cliente cliente) {
        ClienteJPA entity = ClienteMapper.ToEntity(cliente);
        ClienteJPA saved = jpaRepository.save(entity);
        return ClienteMapper.ToDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        return jpaRepository.findById(id)
                .map(ClienteMapper::ToDomain);
    }

    @Override
    public List<Cliente> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(ClienteMapper::ToDomain)
                .toList();
    }

    @Override
    public void eliminarPorId(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Integer id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Cliente> buscarPorDocumentoIdentidad(String documentoIdentidad) {
        return jpaRepository.findByDni(documentoIdentidad)
                .map(ClienteMapper::ToDomain);
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombre(nombre)
                .stream()
                .map(ClienteMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Cliente> buscarPorTipoSeguro(TipoSeguro tipoSeguro) {
        return jpaRepository.findByTipoSeguro(tipoSeguro)
                .stream()
                .map(ClienteMapper::ToDomain)
                .toList();
    }



}
