package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.ConciliacionTresVias;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConciliacionTresViasRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ConciliacionTresViasMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IConciliacionTresViasJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEntradaMercanciaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IFacturaMIROJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IOrdenCompraJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConciliacionTresViasRepositoryImpl implements IConciliacionTresViasRepository {

    private final IConciliacionTresViasJPARepository jpaRepository;
    private final IOrdenCompraJPARepository ordenCompraJPARepository;
    private final IEntradaMercanciaJPARepository entradaMercanciaJPARepository;
    private final IFacturaMIROJPARepository facturaMIROJPARepository;

    public ConciliacionTresViasRepositoryImpl(IConciliacionTresViasJPARepository jpaRepository,
                                               IOrdenCompraJPARepository ordenCompraJPARepository,
                                               IEntradaMercanciaJPARepository entradaMercanciaJPARepository,
                                               IFacturaMIROJPARepository facturaMIROJPARepository) {
        this.jpaRepository = jpaRepository;
        this.ordenCompraJPARepository = ordenCompraJPARepository;
        this.entradaMercanciaJPARepository = entradaMercanciaJPARepository;
        this.facturaMIROJPARepository = facturaMIROJPARepository;
    }

    @Override
    public ConciliacionTresVias save(ConciliacionTresVias conciliacionTresVias) {
        OrdenCompraJPA ordenCompraRef = ordenCompraJPARepository
                .findById(conciliacionTresVias.getOrdenCompra().getId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada"));
        EntradaMercanciaJPA entradaMercanciaRef = entradaMercanciaJPARepository
                .findById(conciliacionTresVias.getEntradaMercancia().getId())
                .orElseThrow(() -> new BadRequestException("Entrada de mercancía (MIGO) no encontrada"));
        FacturaMIROJPA facturaMIRORef = facturaMIROJPARepository
                .findById(conciliacionTresVias.getFacturaMIRO().getId())
                .orElseThrow(() -> new BadRequestException("Factura (MIRO) no encontrada"));

        ConciliacionTresViasJPA entity = ConciliacionTresViasMapper.ToEntity(
                conciliacionTresVias, ordenCompraRef, entradaMercanciaRef, facturaMIRORef);
        ConciliacionTresViasJPA saved = jpaRepository.save(entity);
        return ConciliacionTresViasMapper.ToDomain(saved);
    }

    @Override
    public Optional<ConciliacionTresVias> findById(Long id) {
        return jpaRepository.findById(id).map(ConciliacionTresViasMapper::ToDomain);
    }

    @Override
    public List<ConciliacionTresVias> findAll() {
        return jpaRepository.findAll().stream().map(ConciliacionTresViasMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<ConciliacionTresVias> findByOrdenCompraId(Long ordenCompraId) {
        return jpaRepository.findByOrdenCompra_Id(ordenCompraId).stream()
                .map(ConciliacionTresViasMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByOrdenCompraId(Long ordenCompraId) {
        return jpaRepository.existsByOrdenCompra_Id(ordenCompraId);
    }
}
