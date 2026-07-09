package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Department;
import FarmaciaERP.domain.valueObjects.Ubigeo;

import java.util.List;
import java.util.Optional;

public interface IDepartmentRepository {
    List<Department> All();
    Optional<Department> findById(Long id);
    Optional<Department> findByUbigeoInei(Ubigeo ubigeo);
    Optional<Department> findByUbigeoReniec(Ubigeo ubigeo);
}
