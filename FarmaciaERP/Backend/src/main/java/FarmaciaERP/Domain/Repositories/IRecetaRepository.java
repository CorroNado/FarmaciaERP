package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Entities.Receta;
import FarmaciaERP.Domain.Enums.MedicamentoCategoria;
import FarmaciaERP.Domain.Enums.RecetaEstados;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRecetaRepository {
    Receta save(Receta receta);

    Optional<Receta> findById(int id);

    List<Receta> findAll();

    void deleteById(int id);

    boolean existsById(int id);

    List<Medicamento> findByStatus(RecetaEstados estado);
}
