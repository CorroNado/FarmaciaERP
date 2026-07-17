package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEmpleadoJPARepository extends JpaRepository<EmpleadoJPA, Long> {

    Optional<EmpleadoJPA> findByCodigo(String codigo);

    Optional<EmpleadoJPA> findByDni(String dni);

    Optional<EmpleadoJPA> findByCorreo(String correo);

    List<EmpleadoJPA> findByEstado(EstadoEmpleado estado);

    @Query("SELECT e FROM EmpleadoJPA e WHERE " +
            "LOWER(e.codigo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(e.dni) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(CONCAT(e.apellidoPaterno, ' ', e.apellidoMaterno, ' ', e.nombres)) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(CAST(e.rol AS string)) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<EmpleadoJPA> buscar(@Param("texto") String texto);

    List<EmpleadoJPA> findByEstadoAndBajaProgramadaFechaEfectivaLessThanEqual(EstadoEmpleado estado, LocalDateTime ahora);

    long countByCodigoStartingWith(String prefijo);
}
