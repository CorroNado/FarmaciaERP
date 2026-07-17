package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.RegistroAsistencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRegistroAsistenciaRepository {

    RegistroAsistencia save(RegistroAsistencia registroAsistencia);

    Optional<RegistroAsistencia> findById(Long id);

    List<RegistroAsistencia> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    List<RegistroAsistencia> findByFecha(LocalDate fecha);

    List<RegistroAsistencia> findByEmpleadoId(Long empleadoId);

    List<RegistroAsistencia> findByEmpleadoIdAndMes(Long empleadoId, int mes, int anio);

    /** Registros aún no marcados (ni entrada ni justificados) — usados por el scheduler de estados dinámicos. */
    List<RegistroAsistencia> findPendientesDeMarcacion();
}
