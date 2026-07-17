package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.AsistenciaAuditLogJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAsistenciaAuditLogJPARepository extends JpaRepository<AsistenciaAuditLogJPA, Long> {

    List<AsistenciaAuditLogJPA> findByCodigoEmpleadoOrderByFechaDesc(String codigoEmpleado);

    List<AsistenciaAuditLogJPA> findAllByOrderByFechaDesc();
}
