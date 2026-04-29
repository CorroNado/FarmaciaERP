package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioJPARepository extends JpaRepository<UsuarioJPA, Integer> {
    Optional<UsuarioJPA> findByEmail(String email);

    List<UsuarioJPA> findByEstado(String estado);

    List<UsuarioJPA> findByNombreContainingIgnoreCase(String nombre);
    List<UsuarioJPA> findByNombre(String nombre);

    List<UsuarioJPA>  findByEmailContainingIgnoreCase(String email);
}
