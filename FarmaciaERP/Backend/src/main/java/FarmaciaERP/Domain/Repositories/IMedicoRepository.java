package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Medico;
import FarmaciaERP.Domain.Enums.EspecialidadMedica;

import java.util.List;
import java.util.Optional;

public interface IMedicoRepository {
    Medico save(Medico medico);

    Optional<Medico> findById(int id);

    List<Medico> findAll();

    void deleteById(int id);

    boolean existsById(int id);


    Optional<Medico> findByNumeroColegiatura(String numeroColegiatura);

    List<Medico> findByNombreContaining(String nombre);

    List<Medico> findByEspecialidad(EspecialidadMedica especialidad);
}
