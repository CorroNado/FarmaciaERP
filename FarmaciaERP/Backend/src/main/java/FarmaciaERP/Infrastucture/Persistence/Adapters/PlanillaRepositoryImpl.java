package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Planilla;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPlanillaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PlanillaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.PlanillaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEmpleadoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPlanillaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PlanillaRepositoryImpl implements IPlanillaRepository {

    private final IPlanillaJPARepository jpaRepository;
    private final IEmpleadoJPARepository empleadoJPARepository;

    public PlanillaRepositoryImpl(IPlanillaJPARepository jpaRepository, IEmpleadoJPARepository empleadoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.empleadoJPARepository = empleadoJPARepository;
    }

    @Override
    public Planilla save(Planilla planilla) {
        List<Long> empleadoIds = planilla.getDetalles().stream()
                .map(detalle -> detalle.getEmpleado().getId()).toList();
        List<EmpleadoJPA> empleadosRef = empleadoJPARepository.findAllById(empleadoIds);
        if (empleadosRef.size() != empleadoIds.size()) {
            throw new BadRequestException("Uno o más colaboradores de la planilla no fueron encontrados");
        }
        Map<Long, EmpleadoJPA> empleadosRefById = empleadosRef.stream()
                .collect(Collectors.toMap(EmpleadoJPA::getId, Function.identity()));

        PlanillaJPA entity = PlanillaMapper.ToEntity(planilla, empleadosRefById);
        PlanillaJPA saved = jpaRepository.save(entity);
        return PlanillaMapper.ToDomain(saved);
    }

    @Override
    public Optional<Planilla> findById(Long id) {
        return jpaRepository.findById(id).map(PlanillaMapper::ToDomain);
    }

    @Override
    public List<Planilla> findAll() {
        return jpaRepository.findAll().stream().map(PlanillaMapper::ToDomain).toList();
    }

    @Override
    public Optional<Planilla> findByMesAndAnio(int mes, int anio) {
        return jpaRepository.findByMesAndAnio(mes, anio).map(PlanillaMapper::ToDomain);
    }

    @Override
    public boolean existsByMesAndAnio(int mes, int anio) {
        return jpaRepository.existsByMesAndAnio(mes, anio);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
