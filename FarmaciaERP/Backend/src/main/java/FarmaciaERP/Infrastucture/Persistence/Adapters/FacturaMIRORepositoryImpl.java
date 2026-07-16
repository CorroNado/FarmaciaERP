package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IFacturaMIRORepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.FacturaMIROMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IFacturaMIROJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IOrdenCompraJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FacturaMIRORepositoryImpl implements IFacturaMIRORepository {

    private final IFacturaMIROJPARepository jpaRepository;
    private final IOrdenCompraJPARepository ordenCompraJPARepository;

    public FacturaMIRORepositoryImpl(IFacturaMIROJPARepository jpaRepository,
                                      IOrdenCompraJPARepository ordenCompraJPARepository) {
        this.jpaRepository = jpaRepository;
        this.ordenCompraJPARepository = ordenCompraJPARepository;
    }

    @Override
    public FacturaMIRO save(FacturaMIRO facturaMIRO) {
        OrdenCompraJPA ordenCompraRef = ordenCompraJPARepository.findById(facturaMIRO.getOrdenCompra().getId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada"));

        FacturaMIROJPA entity = FacturaMIROMapper.ToEntity(facturaMIRO, ordenCompraRef);
        FacturaMIROJPA saved = jpaRepository.save(entity);
        return FacturaMIROMapper.ToDomain(saved);
    }

    @Override
    public Optional<FacturaMIRO> findById(Long id) {
        return jpaRepository.findById(id).map(FacturaMIROMapper::ToDomain);
    }

    @Override
    public List<FacturaMIRO> findAll() {
        return jpaRepository.findAll().stream().map(FacturaMIROMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<FacturaMIRO> findByOrdenCompraId(Long ordenCompraId) {
        return jpaRepository.findByOrdenCompra_Id(ordenCompraId).stream().map(FacturaMIROMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByOrdenCompraId(Long ordenCompraId) {
        return jpaRepository.existsByOrdenCompra_Id(ordenCompraId);
    }

    @Override
    public Optional<FacturaMIRO> findByNumeroFactura(String numeroFactura) {
        return jpaRepository.findByNumeroFactura(numeroFactura).map(FacturaMIROMapper::ToDomain);
    }
}
