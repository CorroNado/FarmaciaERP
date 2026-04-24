package FarmaciaERP.Infrastucture.Persistence.Repositories;
import FarmaciaERP.Infrastucture.Persistence.Entities.PacienteJPA;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPacienteJPARepository extends JpaRepository<PacienteJPA,Integer> {


    List<PacienteJPA> findByNombre(String nombre);

    List<PacienteJPA> findByTipoSeguro(TipoSeguro tipoSeguro);

    Optional<PacienteJPA> findByDni(String dni);
}
