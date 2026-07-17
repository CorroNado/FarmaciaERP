package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CuentaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICuentaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CuentaRepositoryImpl implements ICuentaRepository {

    private final ICuentaJPARepository jpaRepository;

    public CuentaRepositoryImpl(ICuentaJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        CuentaJPA entity = CuentaMapper.ToEntity(cuenta);
        CuentaJPA saved = jpaRepository.save(entity);
        return CuentaMapper.ToDomain(saved);
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return jpaRepository.findById(id).map(CuentaMapper::ToDomain);
    }

    @Override
    public Optional<Cuenta> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(CuentaMapper::ToDomain);
    }

    @Override
    public List<Cuenta> findAll() {
        return jpaRepository.findAll().stream().map(CuentaMapper::ToDomain).toList();
    }

    @Override
    public List<Cuenta> findAllActivas() {
        return jpaRepository.findByActivaTrue().stream().map(CuentaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}