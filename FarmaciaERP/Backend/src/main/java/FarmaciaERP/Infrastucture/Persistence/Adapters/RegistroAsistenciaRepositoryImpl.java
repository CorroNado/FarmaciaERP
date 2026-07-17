package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.RegistroAsistenciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.RegistroAsistenciaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEmpleadoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IRegistroAsistenciaJPARepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class RegistroAsistenciaRepositoryImpl implements IRegistroAsistenciaRepository {

    private final IRegistroAsistenciaJPARepository jpaRepository;
    private final IEmpleadoJPARepository empleadoJPARepository;

    public RegistroAsistenciaRepositoryImpl(IRegistroAsistenciaJPARepository jpaRepository,
                                             IEmpleadoJPARepository empleadoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.empleadoJPARepository = empleadoJPARepository;
    }

    @Override
    public RegistroAsistencia save(RegistroAsistencia registroAsistencia) {
        EmpleadoJPA empleadoRef = empleadoJPARepository.findById(registroAsistencia.getEmpleado().getId())
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado"));

        RegistroAsistenciaJPA entity = RegistroAsistenciaMapper.ToEntity(registroAsistencia, empleadoRef);
        RegistroAsistenciaJPA saved = jpaRepository.save(entity);
        return RegistroAsistenciaMapper.ToDomain(saved);
    }

    @Override
    public Optional<RegistroAsistencia> findById(Long id) {
        return jpaRepository.findById(id).map(RegistroAsistenciaMapper::ToDomain);
    }

    @Override
    public List<RegistroAsistencia> findAll() {
        return jpaRepository.findAll().stream().map(RegistroAsistenciaMapper::ToDomain).toList();
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
    public List<RegistroAsistencia> findByFecha(LocalDate fecha) {
        return jpaRepository.findByFecha(fecha).stream().map(RegistroAsistenciaMapper::ToDomain).toList();
    }

    @Override
    public List<RegistroAsistencia> findByEmpleadoId(Long empleadoId) {
        return jpaRepository.findByEmpleado_IdOrderByFechaDesc(empleadoId).stream()
                .map(RegistroAsistenciaMapper::ToDomain).toList();
    }

    @Override
    public List<RegistroAsistencia> findByEmpleadoIdAndMes(Long empleadoId, int mes, int anio) {
        return jpaRepository.findByEmpleadoIdAndMes(empleadoId, mes, anio).stream()
                .map(RegistroAsistenciaMapper::ToDomain).toList();
    }

    @Override
    public List<RegistroAsistencia> findPendientesDeMarcacion() {
        return jpaRepository.findByRegistradoFalseAndJustificadoFalse().stream()
                .map(RegistroAsistenciaMapper::ToDomain).toList();
    }
}
