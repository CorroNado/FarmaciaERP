package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Enums.EstadoEmpleado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IEmpleadoRepository {

    Empleado save(Empleado empleado);

    Optional<Empleado> findById(Long id);

    Optional<Empleado> findByCodigo(String codigo);

    List<Empleado> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<Empleado> findByDni(String dni);

    Optional<Empleado> findByCorreo(String correo);

    List<Empleado> findByEstado(EstadoEmpleado estado);

    List<Empleado> buscar(String texto);

    /** Colaboradores activos con baja programada cuya fecha efectiva ya se cumplió. */
    List<Empleado> findConBajaProgramadaVencida(LocalDateTime ahora);

    String generarSiguienteCodigo();
}
