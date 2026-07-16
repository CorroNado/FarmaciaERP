package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.CobroAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICobroARRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CobroARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CobroARMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICobroARJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IContabilizacionARJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CobroARRepositoryImpl implements ICobroARRepository {

    private final ICobroARJPARepository jpaRepository;
    private final IContabilizacionARJPARepository contabilizacionARJPARepository;

    public CobroARRepositoryImpl(ICobroARJPARepository jpaRepository,
                                  IContabilizacionARJPARepository contabilizacionARJPARepository) {
        this.jpaRepository = jpaRepository;
        this.contabilizacionARJPARepository = contabilizacionARJPARepository;
    }

    @Override
    public CobroAR save(CobroAR cobro) {
        ContabilizacionARJPA contabilizacionARRef = contabilizacionARJPARepository
                .findById(cobro.getContabilizacionAR().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Contabilización AR no encontrada: " + cobro.getContabilizacionAR().getId()));

        CobroARJPA entity = CobroARMapper.ToEntity(cobro, contabilizacionARRef);
        CobroARJPA saved = jpaRepository.save(entity);
        return CobroARMapper.ToDomain(saved);
    }

    @Override
    public Optional<CobroAR> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CobroARMapper::ToDomain);
    }

    @Override
    public Optional<CobroAR> findByContabilizacionARId(Long contabilizacionARId) {
        return jpaRepository.findByContabilizacionAR_Id(contabilizacionARId)
                .map(CobroARMapper::ToDomain);
    }

    @Override
    public List<CobroAR> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(CobroARMapper::ToDomain)
                .toList();
    }
}
