package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Pago;

import java.util.List;
import java.util.Optional;

public interface IPagoRepository {

    Pago save(Pago pago);

    Optional<Pago> findById(Long id);

    List<Pago> findAll();

    boolean existsById(Long id);

    List<Pago> findByFacturaMIROId(Long facturaMIROId);

    boolean existsByFacturaMIROId(Long facturaMIROId);
}
