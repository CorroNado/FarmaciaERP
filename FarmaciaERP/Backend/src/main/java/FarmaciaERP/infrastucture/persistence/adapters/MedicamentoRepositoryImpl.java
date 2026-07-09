package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Medicamento;
import FarmaciaERP.domain.enums.MedicamentoCategoria;
import FarmaciaERP.domain.repositories.IMedicamentoRepository;
import FarmaciaERP.infrastucture.persistence.entities.MedicamentoJPA;
import FarmaciaERP.infrastucture.persistence.mappers.MedicamentoMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IMedicamentoJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MedicamentoRepositoryImpl implements IMedicamentoRepository {

    private final IMedicamentoJPARepository jpaRepository;
    private final MedicamentoMapper medicamentoMapper;

    @Override
    public Medicamento save(Medicamento medicamento) {
        MedicamentoJPA entity = medicamentoMapper.toEntity(medicamento);
        MedicamentoJPA saved = jpaRepository.save(entity);
        return medicamentoMapper.toDomain(saved);
    }

    @Override
    public Optional<Medicamento> findById(int id) {
        return jpaRepository.findById(id)
                .map(medicamentoMapper::toDomain);
    }

    @Override
    public List<Medicamento> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(medicamentoMapper::toDomain)
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
                .map(medicamentoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findByCategoria(MedicamentoCategoria categoria) {
        return jpaRepository.findByCategoria(categoria)
                .stream()
                .map(medicamentoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findByFechaVencimientoBefore(LocalDate fecha) {
        return jpaRepository.findByFechaVencimientoBefore(fecha)
                .stream()
                .map(medicamentoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findByPresentacion(String presentacion) {
        return jpaRepository.findByPresentacion(presentacion)
                .stream()
                .map(medicamentoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Medicamento> findControlados() {
        return jpaRepository.findByCategoria(MedicamentoCategoria.CONTROLADO)
                .stream()
                .map(medicamentoMapper::toDomain)
                .toList();
    }
}
