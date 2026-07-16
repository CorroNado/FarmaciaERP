package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.CierreCaja;

import java.util.List;
import java.util.Optional;

public interface ICierreCajaRepository {

    CierreCaja save(CierreCaja cierreCaja);

    Optional<CierreCaja> findById(Long id);

    List<CierreCaja> findAll();

    List<CierreCaja> findBySucursalId(Long sucursalId);
}
