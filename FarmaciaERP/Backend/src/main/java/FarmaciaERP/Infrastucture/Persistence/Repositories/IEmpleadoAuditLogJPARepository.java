package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoAuditLogJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmpleadoAuditLogJPARepository extends JpaRepository<EmpleadoAuditLogJPA, Long> {

    List<EmpleadoAuditLogJPA> findByCodigoEmpleadoOrderByFechaDesc(String codigoEmpleado);

    List<EmpleadoAuditLogJPA> findAllByOrderByFechaDesc();
}
