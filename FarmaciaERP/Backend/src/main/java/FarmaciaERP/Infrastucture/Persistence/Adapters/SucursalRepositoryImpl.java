package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.SucursalMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISucursalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SucursalRepositoryImpl implements ISucursalRepository {

    private final ISucursalJPARepository jpaRepository;

    public SucursalRepositoryImpl(ISucursalJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        SucursalJPA entity = SucursalMapper.ToEntity(sucursal);
        SucursalJPA saved = jpaRepository.save(entity);
        return SucursalMapper.ToDomain(saved);
    }

    @Override
    public Optional<Sucursal> findById(Long id) {
        return jpaRepository.findById(id).map(SucursalMapper::ToDomain);
    }

    @Override
    public Optional<Sucursal> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(SucursalMapper::ToDomain);
    }

    @Override
    public List<Sucursal> findAll() {
        return jpaRepository.findAll().stream().map(SucursalMapper::ToDomain).toList();
    }

    @Override
    public List<Sucursal> findAllActivas() {
        return jpaRepository.findByActivaTrue().stream().map(SucursalMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}
