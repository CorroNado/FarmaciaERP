package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.InspeccionCalidadJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInspeccionCalidadJPARepository extends JpaRepository<InspeccionCalidadJPA, Long> {

    List<InspeccionCalidadJPA> findByEntradaMercancia_Id(Long entradaMercanciaId);

    boolean existsByEntradaMercancia_Id(Long entradaMercanciaId);
}
