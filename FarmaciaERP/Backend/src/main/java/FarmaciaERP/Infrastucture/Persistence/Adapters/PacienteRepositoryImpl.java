package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IPacienteRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.PacienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPacienteJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PacienteRepositoryImpl implements IPacienteRepository {

    private final IPacienteJPARepository jpaRepository;

    public PacienteRepositoryImpl(IPacienteJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        PacienteJPA entity = mapToEntity(paciente);
        PacienteJPA saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return jpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Paciente> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public void eliminarPorId(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Integer id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Paciente> buscarPorDocumentoIdentidad(String documentoIdentidad) {
        return jpaRepository.findByDni(documentoIdentidad)
                .map(this::mapToDomain);
    }

    @Override
    public List<Paciente> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombre(nombre)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Paciente> buscarPorTipoSeguro(TipoSeguro tipoSeguro) {
        return jpaRepository.findByTipoSeguro(tipoSeguro)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }


    private PacienteJPA mapToEntity(Paciente paciente) {
        PacienteJPA entity = new PacienteJPA();
        entity.setId(paciente.getId());
        entity.setNombre(paciente.getNombre());
        entity.setDni(paciente.getDni());
        entity.setTipoSeguro(paciente.getTipoSeguro());
        return entity;
    }

    private Paciente mapToDomain(PacienteJPA entity) {
        return new Paciente(
                entity.getId(),
                entity.getNombre(),
                entity.getDni(),
                entity.getTipoSeguro()
        );
    }
}
