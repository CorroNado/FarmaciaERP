package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenTrasladoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdenTrasladoJPARepository extends JpaRepository<OrdenTrasladoJPA, Long> {

    List<OrdenTrasladoJPA> findByInspeccionCalidad_Id(Long inspeccionCalidadId);

    List<OrdenTrasladoJPA> findBySucursalDestino_Id(Long sucursalId);

    boolean existsByInspeccionCalidad_Id(Long inspeccionCalidadId);
}
