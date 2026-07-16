package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.FacturaMIRO;

import java.util.List;
import java.util.Optional;

public interface IFacturaMIRORepository {

    FacturaMIRO save(FacturaMIRO facturaMIRO);

    Optional<FacturaMIRO> findById(Long id);

    List<FacturaMIRO> findAll();

    boolean existsById(Long id);

    List<FacturaMIRO> findByOrdenCompraId(Long ordenCompraId);

    boolean existsByOrdenCompraId(Long ordenCompraId);

    Optional<FacturaMIRO> findByNumeroFactura(String numeroFactura);
}
