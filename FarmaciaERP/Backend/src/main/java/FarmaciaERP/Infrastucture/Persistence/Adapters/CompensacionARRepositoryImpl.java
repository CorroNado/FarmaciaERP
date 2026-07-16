package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.CompensacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICompensacionARRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CompensacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CompensacionARMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICompensacionARJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IContabilizacionARJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompensacionARRepositoryImpl implements ICompensacionARRepository {

    private final ICompensacionARJPARepository jpaRepository;
    private final IContabilizacionARJPARepository contabilizacionARJPARepository;

    public CompensacionARRepositoryImpl(ICompensacionARJPARepository jpaRepository,
                                         IContabilizacionARJPARepository contabilizacionARJPARepository) {
        this.jpaRepository = jpaRepository;
        this.contabilizacionARJPARepository = contabilizacionARJPARepository;
    }

    @Override
    public CompensacionAR save(CompensacionAR compensacion) {
        ContabilizacionARJPA contabilizacionARRef = contabilizacionARJPARepository
                .findById(compensacion.getContabilizacionAR().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Contabilización AR no encontrada: " + compensacion.getContabilizacionAR().getId()));

        CompensacionARJPA entity = CompensacionARMapper.ToEntity(compensacion, contabilizacionARRef);
        CompensacionARJPA saved = jpaRepository.save(entity);
        return CompensacionARMapper.ToDomain(saved);
    }

    @Override
    public Optional<CompensacionAR> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CompensacionARMapper::ToDomain);
    }

    @Override
    public Optional<CompensacionAR> findByContabilizacionARId(Long contabilizacionARId) {
        return jpaRepository.findByContabilizacionAR_Id(contabilizacionARId)
                .map(CompensacionARMapper::ToDomain);
    }

    @Override
    public List<CompensacionAR> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(CompensacionARMapper::ToDomain)
                .toList();
    }
}
