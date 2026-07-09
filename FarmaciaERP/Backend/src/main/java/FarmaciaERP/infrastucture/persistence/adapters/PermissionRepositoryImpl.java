package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Permission;
import FarmaciaERP.domain.repositories.IPermissionRepository;
import FarmaciaERP.infrastucture.persistence.mappers.PermissionMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IPermissionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements IPermissionRepository {
    private final PermissionMapper permissionMapper;
    private final IPermissionJPARepository jpaRepository;

    @Override
    public List<Permission> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(permissionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Permission> findByCode(String code) {
        return jpaRepository.findByCodigo(code)
                .map(permissionMapper::toDomain);
    }
}
