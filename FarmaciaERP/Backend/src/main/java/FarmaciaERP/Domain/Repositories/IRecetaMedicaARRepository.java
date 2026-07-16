package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.RecetaMedicaAR;

import java.util.List;
import java.util.Optional;

public interface IRecetaMedicaARRepository {

    RecetaMedicaAR save(RecetaMedicaAR receta);

    Optional<RecetaMedicaAR> findById(Long id);

    List<RecetaMedicaAR> findByContabilizacionARId(Long contabilizacionARId);

    List<RecetaMedicaAR> findAll();
}
