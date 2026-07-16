package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.DebitoAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDebitoARRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.DebitoARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.RecetaMedicaARJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.DebitoARMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IDebitoARJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IRecetaMedicaARJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DebitoARRepositoryImpl implements IDebitoARRepository {

    private final IDebitoARJPARepository jpaRepository;
    private final IRecetaMedicaARJPARepository recetaMedicaARJPARepository;

    public DebitoARRepositoryImpl(IDebitoARJPARepository jpaRepository,
                                   IRecetaMedicaARJPARepository recetaMedicaARJPARepository) {
        this.jpaRepository = jpaRepository;
        this.recetaMedicaARJPARepository = recetaMedicaARJPARepository;
    }

    @Override
    public DebitoAR save(DebitoAR debito) {
        RecetaMedicaARJPA recetaMedicaARRef = recetaMedicaARJPARepository
                .findById(debito.getRecetaMedicaAR().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Receta médica AR no encontrada: " + debito.getRecetaMedicaAR().getId()));

        DebitoARJPA entity = DebitoARMapper.ToEntity(debito, recetaMedicaARRef);
        DebitoARJPA saved = jpaRepository.save(entity);
        return DebitoARMapper.ToDomain(saved);
    }

    @Override
    public Optional<DebitoAR> findById(Long id) {
        return jpaRepository.findById(id)
                .map(DebitoARMapper::ToDomain);
    }

    @Override
    public Optional<DebitoAR> findByRecetaMedicaARId(Long recetaMedicaARId) {
        return jpaRepository.findByRecetaMedicaAR_Id(recetaMedicaARId)
                .map(DebitoARMapper::ToDomain);
    }

    @Override
    public List<DebitoAR> findByContabilizacionARId(Long contabilizacionARId) {
        return jpaRepository.findByContabilizacionARId(contabilizacionARId)
                .stream()
                .map(DebitoARMapper::ToDomain)
                .toList();
    }

    @Override
    public List<DebitoAR> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(DebitoARMapper::ToDomain)
                .toList();
    }
}
