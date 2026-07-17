package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.RegistroAsistenciaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IRegistroAsistenciaJPARepository extends JpaRepository<RegistroAsistenciaJPA, Long> {

    List<RegistroAsistenciaJPA> findByFecha(LocalDate fecha);

    List<RegistroAsistenciaJPA> findByEmpleado_IdOrderByFechaDesc(Long empleadoId);

    @Query("SELECT r FROM RegistroAsistenciaJPA r WHERE r.empleado.id = :empleadoId "
            + "AND MONTH(r.fecha) = :mes AND YEAR(r.fecha) = :anio ORDER BY r.fecha")
    List<RegistroAsistenciaJPA> findByEmpleadoIdAndMes(@Param("empleadoId") Long empleadoId,
                                                        @Param("mes") int mes, @Param("anio") int anio);

    List<RegistroAsistenciaJPA> findByRegistradoFalseAndJustificadoFalse();
}
