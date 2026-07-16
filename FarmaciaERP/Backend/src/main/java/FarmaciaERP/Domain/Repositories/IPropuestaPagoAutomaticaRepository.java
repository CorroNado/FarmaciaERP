package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;

import java.util.List;
import java.util.Optional;

public interface IPropuestaPagoAutomaticaRepository {

    PropuestaPagoAutomatica save(PropuestaPagoAutomatica propuestaPagoAutomatica);

    Optional<PropuestaPagoAutomatica> findById(Long id);

    List<PropuestaPagoAutomatica> findAll();

    boolean existsById(Long id);

    boolean existsByLotePagoTesoreriaId(Long lotePagoTesoreriaId);
}
