package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.EmpleadoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEmpleadoJPARepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoRepositoryImpl implements IEmpleadoRepository {

    private final IEmpleadoJPARepository jpaRepository;

    public EmpleadoRepositoryImpl(IEmpleadoJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Empleado save(Empleado empleado) {
        EmpleadoJPA entity = EmpleadoMapper.ToEntity(empleado);
        EmpleadoJPA saved = jpaRepository.save(entity);
        return EmpleadoMapper.ToDomain(saved);
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return jpaRepository.findById(id).map(EmpleadoMapper::ToDomain);
    }

    @Override
    public Optional<Empleado> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(EmpleadoMapper::ToDomain);
    }

    @Override
    public List<Empleado> findAll() {
        return jpaRepository.findAll().stream().map(EmpleadoMapper::ToDomain).toList();
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
    public Optional<Empleado> findByDni(String dni) {
        return jpaRepository.findByDni(dni).map(EmpleadoMapper::ToDomain);
    }

    @Override
    public Optional<Empleado> findByCorreo(String correo) {
        return jpaRepository.findByCorreo(correo).map(EmpleadoMapper::ToDomain);
    }

    @Override
    public List<Empleado> findByEstado(EstadoEmpleado estado) {
        return jpaRepository.findByEstado(estado).stream().map(EmpleadoMapper::ToDomain).toList();
    }

    @Override
    public List<Empleado> buscar(String texto) {
        return jpaRepository.buscar(texto).stream().map(EmpleadoMapper::ToDomain).toList();
    }

    @Override
    public List<Empleado> findConBajaProgramadaVencida(LocalDateTime ahora) {
        return jpaRepository.findByEstadoAndBajaProgramadaFechaEfectivaLessThanEqual(EstadoEmpleado.ACTIVO, ahora)
                .stream().map(EmpleadoMapper::ToDomain).toList();
    }

    @Override
    public String generarSiguienteCodigo() {
        long total = jpaRepository.count();
        String candidato;
        long siguiente = total + 1;
        do {
            candidato = "EMP-" + String.format("%03d", siguiente);
            siguiente++;
        } while (jpaRepository.findByCodigo(candidato).isPresent());
        return candidato;
    }
}
