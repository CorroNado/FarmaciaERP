package FarmaciaERP.Infrastucture.Persistence.Repositories;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.DniEmb;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClienteJPARepository extends JpaRepository<ClienteJPA,Integer> {


    List<ClienteJPA> findByTipoSeguro(TipoSeguro tipoSeguro);


    Optional<ClienteJPA> findByNombres(FullNameEmb nombres);

    Optional<ClienteJPA> findByDni_Dni(String dniDni);
}
