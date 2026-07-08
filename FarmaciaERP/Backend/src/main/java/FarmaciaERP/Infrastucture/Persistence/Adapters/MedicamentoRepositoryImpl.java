package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Enums.MedicamentoCategoria;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.MedicamentoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IMedicamentoJPARepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicamentoRepositoryImpl implements IMedicamentoRepository {

    private final IMedicamentoJPARepository jpaRepository;

    public MedicamentoRepositoryImpl(IMedicamentoJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Medicamento save(Medicamento medicamento) {
        MedicamentoJPA entity = MedicamentoMapper.ToEntity(medicamento);
        MedicamentoJPA saved = jpaRepository.save(entity);
        return MedicamentoMapper.ToDomain(saved);
    }

    @Override
    public Optional<Medicamento> findById(int id) {
        return jpaRepository.findById(id)
                .map(MedicamentoMapper::ToDomain);
    }

    @Override
    public List<Medicamento> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(MedicamentoMapper::ToDomain)
                .toList();
    }

    @Override
    public void deleteById(int id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Medicamento> findByNombre(String nombre) {
        return jpaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(MedicamentoMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findByCategoria(MedicamentoCategoria categoria) {
        return jpaRepository.findByCategoria(categoria)
                .stream()
                .map(MedicamentoMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findByFechaVencimientoBefore(LocalDate fecha) {
        return jpaRepository.findByFechaVencimientoBefore(fecha)
                .stream()
                .map(MedicamentoMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findByPresentacion(String presentacion) {
        return jpaRepository.findByPresentacion(presentacion)
                .stream()
                .map(MedicamentoMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findControlados() {
        return jpaRepository.findByCategoria(MedicamentoCategoria.CONTROLADO)
                .stream()
                .map(MedicamentoMapper::ToDomain)
                .toList();
    }
}
