package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.infrastucture.persistence.entities.UserJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserJPARepository extends JpaRepository<UserJPA, Long> {
    @Query("SELECT u FROM UserJPA u WHERE u.loginSeguro.estado = :estado")
    List<UserJPA> findByEstado(@Param("estado") String estado);
    @Query("SELECT u FROM UserJPA u WHERE u.nombreCompleto.valor = :nombreCompleto")
    List<UserJPA> findByNombres(@Param("nombreCompleto") String nombreCompleto);
    @Query("SELECT u FROM UserJPA u JOIN u.emails e WHERE e.direccion.direccion = :emailAddress")
    Optional<UserJPA> findByEmail(@Param("emailAddress") String emailAddress);
    @Query("SELECT u FROM UserJPA u JOIN  u.telefonos t WHERE t.numeroCompleto = :numero")
    Optional<UserJPA> findByTelephone(@Param("numero") String numeroCompleto);
    @Query("SELECT u FROM UserJPA u WHERE u.username.valor = :username")
    Optional<UserJPA> findByUsername(@Param("username")String username);
    @Query("SELECT u FROM UserJPA u JOIN u.perfil p WHERE p.perfilId = :perfilId")
    List<UserJPA> findByProfile(@Param("perfilId") Long perfilId);
    @Query("SELECT u FROM UserJPA u JOIN u.direcciones d JOIN d.distrito di JOIN di.provincia p JOIN p.departamento de WHERE de.departamentoId = :departamentoId")
    List<UserJPA> findByDepartment(@Param("departamentoId") Long departamentoId);
    @Query("SELECT u FROM UserJPA u JOIN u.direcciones d JOIN d.distrito di JOIN di.provincia p  WHERE p.provinciaId = :provinciaId")
    List<UserJPA> findByProvince(@Param("provinciaId") Long provinciaId);
    @Query("SELECT u FROM UserJPA u JOIN u.direcciones d JOIN d.distrito di WHERE di.distritoId = :distritoId")
    List<UserJPA> findByDistrict(@Param("distritoId") Long distritoId);
}
