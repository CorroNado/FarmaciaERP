package FarmaciaERP.infrastucture.persistence.repositories;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.infrastucture.persistence.entities.ClienteJPA;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.infrastucture.persistence.embeddable.DniEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.FullNameEmb;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClienteJPARepository extends JpaRepository<ClienteJPA,Long> {


    List<ClienteJPA> findByInsuranceType(InsuranceType InsuranceType);


    Optional<ClienteJPA> findByNombres(FullNameEmb nombres);

    Optional<ClienteJPA> findByDni_Dni(String dniDni);
}
