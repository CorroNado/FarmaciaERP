package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.LotePagoTesoreriaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ILotePagoTesoreriaJPARepository extends JpaRepository<LotePagoTesoreriaJPA, Long> {

    @Query("SELECT COUNT(l) > 0 FROM LotePagoTesoreriaJPA l JOIN l.ajustesContables a WHERE a.id = :ajusteContableId")
    boolean existsByAjusteContableRegularizacionId(@Param("ajusteContableId") Long ajusteContableId);
}
