package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionRepository {
    List<Permission> findAll();
    Optional<Permission> findByCode(String code);
}
