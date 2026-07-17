package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Planilla;

import java.util.List;
import java.util.Optional;

public interface IPlanillaRepository {

    Planilla save(Planilla planilla);

    Optional<Planilla> findById(Long id);

    List<Planilla> findAll();

    Optional<Planilla> findByMesAndAnio(int mes, int anio);

    boolean existsByMesAndAnio(int mes, int anio);

    void deleteById(Long id);

    boolean existsById(Long id);
}
