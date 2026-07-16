package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IExcepcionFacturacionRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ExcepcionFacturacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ExcepcionFacturacionMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IConciliacionTresViasJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IExcepcionFacturacionJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExcepcionFacturacionRepositoryImpl implements IExcepcionFacturacionRepository {

    private final IExcepcionFacturacionJPARepository jpaRepository;
    private final IConciliacionTresViasJPARepository conciliacionTresViasJPARepository;

    public ExcepcionFacturacionRepositoryImpl(IExcepcionFacturacionJPARepository jpaRepository,
                                               IConciliacionTresViasJPARepository conciliacionTresViasJPARepository) {
        this.jpaRepository = jpaRepository;
        this.conciliacionTresViasJPARepository = conciliacionTresViasJPARepository;
    }

    @Override
    public ExcepcionFacturacion save(ExcepcionFacturacion excepcionFacturacion) {
        ConciliacionTresViasJPA conciliacionRef = conciliacionTresViasJPARepository
                .findById(excepcionFacturacion.getConciliacionTresVias().getId())
                .orElseThrow(() -> new BadRequestException("Conciliación de 3 vías (MRBR) no encontrada"));

        ExcepcionFacturacionJPA entity = ExcepcionFacturacionMapper.ToEntity(excepcionFacturacion, conciliacionRef);
        ExcepcionFacturacionJPA saved = jpaRepository.save(entity);
        return ExcepcionFacturacionMapper.ToDomain(saved);
    }

    @Override
    public Optional<ExcepcionFacturacion> findById(Long id) {
        return jpaRepository.findById(id).map(ExcepcionFacturacionMapper::ToDomain);
    }

    @Override
    public List<ExcepcionFacturacion> findAll() {
        return jpaRepository.findAll().stream().map(ExcepcionFacturacionMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<ExcepcionFacturacion> findByConciliacionTresViasId(Long conciliacionTresViasId) {
        return jpaRepository.findByConciliacionTresVias_Id(conciliacionTresViasId).stream()
                .map(ExcepcionFacturacionMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByConciliacionTresViasId(Long conciliacionTresViasId) {
        return jpaRepository.existsByConciliacionTresVias_Id(conciliacionTresViasId);
    }
}
