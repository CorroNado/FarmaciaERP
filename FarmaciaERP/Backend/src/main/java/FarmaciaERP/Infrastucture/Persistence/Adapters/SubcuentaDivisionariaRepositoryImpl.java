package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.SubcuentaDivisionariaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICuentaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISubcuentaDivisionariaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubcuentaDivisionariaRepositoryImpl implements ISubcuentaDivisionariaRepository {

    private final ISubcuentaDivisionariaJPARepository jpaRepository;
    private final ICuentaJPARepository cuentaJPARepository;

    public SubcuentaDivisionariaRepositoryImpl(ISubcuentaDivisionariaJPARepository jpaRepository,
                                                ICuentaJPARepository cuentaJPARepository) {
        this.jpaRepository = jpaRepository;
        this.cuentaJPARepository = cuentaJPARepository;
    }

    @Override
    public SubcuentaDivisionaria save(SubcuentaDivisionaria subcuenta) {
        CuentaJPA cuentaRef = cuentaJPARepository.findById(subcuenta.getCuenta().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Cuenta no encontrada: " + subcuenta.getCuenta().getId()));

        SubcuentaDivisionariaJPA entity = SubcuentaDivisionariaMapper.ToEntity(subcuenta, cuentaRef);
        SubcuentaDivisionariaJPA saved = jpaRepository.save(entity);
        return SubcuentaDivisionariaMapper.ToDomain(saved);
    }

    @Override
    public Optional<SubcuentaDivisionaria> findById(Long id) {
        return jpaRepository.findById(id).map(SubcuentaDivisionariaMapper::ToDomain);
    }

    @Override
    public Optional<SubcuentaDivisionaria> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(SubcuentaDivisionariaMapper::ToDomain);
    }

    @Override
    public List<SubcuentaDivisionaria> findAll() {
        return jpaRepository.findAll().stream().map(SubcuentaDivisionariaMapper::ToDomain).toList();
    }

    @Override
    public List<SubcuentaDivisionaria> findByCuentaId(Long cuentaId) {
        return jpaRepository.findByCuenta_Id(cuentaId).stream().map(SubcuentaDivisionariaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}