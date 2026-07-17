package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.AsistenciaAuditLog;
import FarmaciaERP.Domain.Repositories.IAsistenciaAuditLogRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsistenciaAuditLogJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.AsistenciaAuditLogMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IAsistenciaAuditLogJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AsistenciaAuditLogRepositoryImpl implements IAsistenciaAuditLogRepository {

    private final IAsistenciaAuditLogJPARepository jpaRepository;

    public AsistenciaAuditLogRepositoryImpl(IAsistenciaAuditLogJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AsistenciaAuditLog save(AsistenciaAuditLog log) {
        AsistenciaAuditLogJPA entity = AsistenciaAuditLogMapper.ToEntity(log);
        AsistenciaAuditLogJPA saved = jpaRepository.save(entity);
        return AsistenciaAuditLogMapper.ToDomain(saved);
    }

    @Override
    public List<AsistenciaAuditLog> findAll() {
        return jpaRepository.findAllByOrderByFechaDesc().stream().map(AsistenciaAuditLogMapper::ToDomain).toList();
    }

    @Override
    public List<AsistenciaAuditLog> findByCodigoEmpleado(String codigoEmpleado) {
        return jpaRepository.findByCodigoEmpleadoOrderByFechaDesc(codigoEmpleado)
                .stream().map(AsistenciaAuditLogMapper::ToDomain).toList();
    }
}
