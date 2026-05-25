package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserJPARepository extends JpaRepository<UsuarioJPA, Long> {

    List<UsuarioJPA> findByEstado(String estado);

    List<UsuarioJPA> findByNombres_Value(String fullname);

    @Query("SELECT u FROM UsuarioJPA u WHERE u.email.email = :email")
    Optional<UsuarioJPA> findByEmail_Email(@Param("direccion") String email);
}
