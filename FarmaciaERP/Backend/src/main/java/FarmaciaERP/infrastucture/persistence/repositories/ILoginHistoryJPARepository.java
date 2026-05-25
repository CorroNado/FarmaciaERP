package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.LoginHistoryJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoginHistoryJPARepository extends JpaRepository<LoginHistoryJPA, Long> {
    List<LoginHistoryJPA> findByUsuario_Id(Long usuarioId);
}
