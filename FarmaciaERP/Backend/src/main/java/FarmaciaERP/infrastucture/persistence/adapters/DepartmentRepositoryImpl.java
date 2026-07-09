package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Department;
import FarmaciaERP.domain.repositories.IDepartmentRepository;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import FarmaciaERP.infrastucture.persistence.mappers.DepartmentMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IDepartmentJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements IDepartmentRepository {
    private final IDepartmentJPARepository jpaRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<Department> All() {
        return jpaRepository.findAll()
                .stream()
                .map(departmentMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Department> findById(Long id) {
        return jpaRepository.findById(id)
                .map(departmentMapper::toDomain);
    }

    @Override
    public Optional<Department> findByUbigeoInei(Ubigeo ubigeo) {
        return jpaRepository.findByUbigeoInei(ubigeo.getUbigeoInei())
                .map(departmentMapper::toDomain);
    }

    @Override
    public Optional<Department> findByUbigeoReniec(Ubigeo ubigeo) {
        return jpaRepository.findByUbigeoReniec(ubigeo.getUbigeoReniec())
                .map(departmentMapper::toDomain);
    }
}
