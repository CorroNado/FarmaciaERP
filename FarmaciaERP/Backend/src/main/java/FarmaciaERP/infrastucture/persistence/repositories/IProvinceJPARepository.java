package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.DepartmentJPA;
import FarmaciaERP.infrastucture.persistence.entities.ProvinceJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IProvinceJPARepository extends JpaRepository<ProvinceJPA,Long> {
    @Query("SELECT p FROM ProvinceJPA p WHERE p.ubigeo.ubigeoInei = :ubigeoInei")
    Optional<ProvinceJPA> findByUbigeoInei(@Param("ubigeoInei") String ubigeoInei);

    @Query("SELECT p FROM ProvinceJPA p WHERE p.ubigeo.ubigeoReniec = :ubigeoReniec")
    Optional<ProvinceJPA> findByUbigeoReniec(@Param("ubigeoReniec") String ubigeoReniec);
}
