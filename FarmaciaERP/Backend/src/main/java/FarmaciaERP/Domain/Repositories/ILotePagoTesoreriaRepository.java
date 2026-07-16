package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.LotePagoTesoreria;

import java.util.List;
import java.util.Optional;

public interface ILotePagoTesoreriaRepository {

    LotePagoTesoreria save(LotePagoTesoreria lotePagoTesoreria);

    Optional<LotePagoTesoreria> findById(Long id);

    List<LotePagoTesoreria> findAll();

    boolean existsById(Long id);

    boolean existsByAjusteContableRegularizacionId(Long ajusteContableId);
}
