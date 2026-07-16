package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.RecetaMedicaARJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.RecetaMedicaARMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IContabilizacionARJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IRecetaMedicaARJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecetaMedicaARRepositoryImpl implements IRecetaMedicaARRepository {

    private final IRecetaMedicaARJPARepository jpaRepository;
    private final IContabilizacionARJPARepository contabilizacionARJPARepository;

    public RecetaMedicaARRepositoryImpl(IRecetaMedicaARJPARepository jpaRepository,
                                         IContabilizacionARJPARepository contabilizacionARJPARepository) {
        this.jpaRepository = jpaRepository;
        this.contabilizacionARJPARepository = contabilizacionARJPARepository;
    }

    @Override
    public RecetaMedicaAR save(RecetaMedicaAR receta) {
        ContabilizacionARJPA contabilizacionARRef = contabilizacionARJPARepository
                .findById(receta.getContabilizacionAR().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Contabilización AR no encontrada: " + receta.getContabilizacionAR().getId()));

        RecetaMedicaARJPA entity = RecetaMedicaARMapper.ToEntity(receta, contabilizacionARRef);
        RecetaMedicaARJPA saved = jpaRepository.save(entity);
        return RecetaMedicaARMapper.ToDomain(saved);
    }

    @Override
    public Optional<RecetaMedicaAR> findById(Long id) {
        return jpaRepository.findById(id)
                .map(RecetaMedicaARMapper::ToDomain);
    }

    @Override
    public List<RecetaMedicaAR> findByContabilizacionARId(Long contabilizacionARId) {
        return jpaRepository.findByContabilizacionAR_Id(contabilizacionARId)
                .stream()
                .map(RecetaMedicaARMapper::ToDomain)
                .toList();
    }

    @Override
    public List<RecetaMedicaAR> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(RecetaMedicaARMapper::ToDomain)
                .toList();
    }
}
