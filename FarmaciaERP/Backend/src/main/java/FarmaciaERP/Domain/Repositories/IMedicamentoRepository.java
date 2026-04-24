package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Enums.MedicamentoCategoria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IMedicamentoRepository {

    Medicamento save(Medicamento medicamento);

    Optional<Medicamento> findById(int id);

    List<Medicamento> findAll();

    void deleteById(int id);

    boolean existsById(int id);

    List<Medicamento> findByNombre(String nombre);

    List<Medicamento> findByCategoria(MedicamentoCategoria categoria);

    List<Medicamento> findByFechaVencimientoBefore(LocalDate fecha);

    List<Medicamento> findByPresentacion(String presentacion);

    List<Medicamento> findControlados();
}
