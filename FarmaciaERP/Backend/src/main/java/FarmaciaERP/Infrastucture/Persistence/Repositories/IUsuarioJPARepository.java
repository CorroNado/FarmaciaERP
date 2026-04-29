package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuarioJPARepository extends JpaRepository<UsuarioJPA, Integer> {

    List<UsuarioJPA> findByEstado(String estado);

    List<UsuarioJPA> findByNombresContainingIgnoreCase(FullNameEmb nombres);

    List<UsuarioJPA>  findByEmailContainingIgnoreCase(String email);
}
