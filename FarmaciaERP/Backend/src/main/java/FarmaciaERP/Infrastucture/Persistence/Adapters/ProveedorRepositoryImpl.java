package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Domain.Enums.EstadoProveedor;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ProveedorMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IProveedorJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProveedorRepositoryImpl implements IProveedorRepository {

    private final IProveedorJPARepository jpaRepository;

    public ProveedorRepositoryImpl(IProveedorJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        ProveedorJPA entity = ProveedorMapper.ToEntity(proveedor);
        ProveedorJPA saved = jpaRepository.save(entity);
        return ProveedorMapper.ToDomain(saved);
    }

    @Override
    public Optional<Proveedor> findById(Long id) {
        return jpaRepository.findById(id).map(ProveedorMapper::ToDomain);
    }

    @Override
    public List<Proveedor> findAll() {
        return jpaRepository.findAll().stream().map(ProveedorMapper::ToDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Proveedor> findByRuc(String ruc) {
        return jpaRepository.findByRuc(ruc).map(ProveedorMapper::ToDomain);
    }

    @Override
    public List<Proveedor> findByRazonSocial(String razonSocial) {
        return jpaRepository.findByRazonSocialContainingIgnoreCase(razonSocial)
                .stream().map(ProveedorMapper::ToDomain).toList();
    }

    @Override
    public List<Proveedor> findByEstado(EstadoProveedor estado) {
        return jpaRepository.findByEstado(estado).stream().map(ProveedorMapper::ToDomain).toList();
    }
}
