package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.LoginHistoryJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoginHistoryJPARepository extends JpaRepository<LoginHistoryJPA, Long> {
    @Query("SELECT l FROM LoginHistoryJPA l JOIN l.usuario u WHERE u.userId = :usuarioId")
    List<LoginHistoryJPA> findByUser(@Param("usuarioId") Long usuarioId);
}
