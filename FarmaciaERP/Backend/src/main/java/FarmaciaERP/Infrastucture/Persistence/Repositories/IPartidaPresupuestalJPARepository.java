package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.PartidaPresupuestalJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPartidaPresupuestalJPARepository extends JpaRepository<PartidaPresupuestalJPA, Long> {

    List<PartidaPresupuestalJPA> findByCentroCosto_Id(Long centroCostoId);

    boolean existsByCodigo(String codigo);
}