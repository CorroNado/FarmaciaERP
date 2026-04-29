package FarmaciaERP.Infrastucture.Persistence.Repositories;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClienteJPARepository extends JpaRepository<ClienteJPA,Integer> {


    List<ClienteJPA> findByNombre(String nombre);

    List<ClienteJPA> findByTipoSeguro(TipoSeguro tipoSeguro);

    Optional<ClienteJPA> findByDni(String dni);
}
