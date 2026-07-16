package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CierreCajaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICierreCajaJPARepository extends JpaRepository<CierreCajaJPA, Long> {

    List<CierreCajaJPA> findBySucursal_Id(Long sucursalId);
}
