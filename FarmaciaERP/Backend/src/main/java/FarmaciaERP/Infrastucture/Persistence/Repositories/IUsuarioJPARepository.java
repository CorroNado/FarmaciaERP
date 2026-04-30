package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioJPARepository extends JpaRepository<UsuarioJPA, Long> {

    List<UsuarioJPA> findByEstado(String estado);

    List<UsuarioJPA> findByNombresContainingIgnoreCase(FullNameEmb nombres);

    @Query("SELECT u FROM UsuarioJPA u WHERE u.email.email = :email")
    Optional<UsuarioJPA> findByEmail_Email(@Param("email") String email);
}
