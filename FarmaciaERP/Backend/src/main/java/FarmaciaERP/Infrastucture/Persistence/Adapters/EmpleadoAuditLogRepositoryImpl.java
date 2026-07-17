package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.EmpleadoAuditLog;
import FarmaciaERP.Domain.Repositories.IEmpleadoAuditLogRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoAuditLogJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.EmpleadoAuditLogMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEmpleadoAuditLogJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmpleadoAuditLogRepositoryImpl implements IEmpleadoAuditLogRepository {

    private final IEmpleadoAuditLogJPARepository jpaRepository;

    public EmpleadoAuditLogRepositoryImpl(IEmpleadoAuditLogJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public EmpleadoAuditLog save(EmpleadoAuditLog log) {
        EmpleadoAuditLogJPA entity = EmpleadoAuditLogMapper.ToEntity(log);
        EmpleadoAuditLogJPA saved = jpaRepository.save(entity);
        return EmpleadoAuditLogMapper.ToDomain(saved);
    }

    @Override
    public List<EmpleadoAuditLog> findAll() {
        return jpaRepository.findAllByOrderByFechaDesc().stream().map(EmpleadoAuditLogMapper::ToDomain).toList();
    }

    @Override
    public List<EmpleadoAuditLog> findByCodigoEmpleado(String codigoEmpleado) {
        return jpaRepository.findByCodigoEmpleadoOrderByFechaDesc(codigoEmpleado)
                .stream().map(EmpleadoAuditLogMapper::ToDomain).toList();
    }
}
