package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ClienteMapper;
import FarmaciaERP.Infrastucture.Persistence.Mappers.DniMapper;
import FarmaciaERP.Infrastucture.Persistence.Mappers.FullnameMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IClienteJPARepository;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
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
    public Optional<Cliente> buscarPorDocumentoIdentidad(Dni documentoIdentidad) {
        return jpaRepository.findByDni_Dni(documentoIdentidad.getDni())
                .map(ClienteMapper::ToDomain);
    }

    @Override
    public List<Cliente> buscarPorNombres(FullName nombres) {
        return jpaRepository.findByNombres(FullnameMapper.toEmbeddable(nombres))
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
