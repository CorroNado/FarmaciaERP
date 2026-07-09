package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.infrastucture.persistence.entities.EmailContactJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEmailJPARepository extends JpaRepository<EmailContactJPA, Long> {

    @Query("SELECT e FROM EmailContactJPA e WHERE e.ownerId = :ownerId AND e.ownerType = :ownerType")
    List<EmailContactJPA> findByOwnerId(@Param("ownerId") Long ownerId,@Param("ownerType") OwnerType ownerType);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EmailContactJPA e WHERE e.direccion.direccion = :direccion")
    boolean existsByEmailAddress(@Param("direccion") String direccion);
}
