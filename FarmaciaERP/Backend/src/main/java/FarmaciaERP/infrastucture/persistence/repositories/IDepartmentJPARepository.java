package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.domain.entities.Department;
import FarmaciaERP.infrastucture.persistence.entities.DepartmentJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IDepartmentJPARepository extends JpaRepository<DepartmentJPA,Long> {
    @Query("SELECT d FROM DepartmentJPA d WHERE d.ubigeo.ubigeoInei = :ubigeoInei")
    Optional<DepartmentJPA> findByUbigeoInei(@Param("ubigeoInei") String ubigeoInei);

    @Query("SELECT d FROM DepartmentJPA d WHERE d.ubigeo.ubigeoReniec = :ubigeoReniec")
    Optional<DepartmentJPA> findByUbigeoReniec(@Param("ubigeoReniec") String ubigeoReniec);
}
