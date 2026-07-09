package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.DepartmentJPA;
import FarmaciaERP.infrastucture.persistence.entities.DistrictJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IDistrictJPARepository extends JpaRepository<DistrictJPA,Long> {
    @Query("SELECT d FROM DistrictJPA d WHERE d.ubigeo.ubigeoInei = :ubigeoInei")
    Optional<DistrictJPA> findByUbigeoInei(@Param("ubigeoInei") String ubigeoInei);

    @Query("SELECT d FROM DistrictJPA d WHERE d.ubigeo.ubigeoReniec = :ubigeoReniec")
    Optional<DistrictJPA> findByUbigeoReniec(@Param("ubigeoReniec") String ubigeoReniec);
}
