package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.EntradaMercancia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.EntradaMercanciaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEntradaMercanciaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IOrdenCompraJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EntradaMercanciaRepositoryImpl implements IEntradaMercanciaRepository {

    private final IEntradaMercanciaJPARepository jpaRepository;
    private final IOrdenCompraJPARepository ordenCompraJPARepository;

    public EntradaMercanciaRepositoryImpl(IEntradaMercanciaJPARepository jpaRepository,
                                           IOrdenCompraJPARepository ordenCompraJPARepository) {
        this.jpaRepository = jpaRepository;
        this.ordenCompraJPARepository = ordenCompraJPARepository;
    }

    @Override
    public EntradaMercancia save(EntradaMercancia entradaMercancia) {
        OrdenCompraJPA ordenCompraRef = ordenCompraJPARepository.findById(entradaMercancia.getOrdenCompra().getId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada"));

        EntradaMercanciaJPA entity = EntradaMercanciaMapper.ToEntity(entradaMercancia, ordenCompraRef);
        EntradaMercanciaJPA saved = jpaRepository.save(entity);
        return EntradaMercanciaMapper.ToDomain(saved);
    }

    @Override
    public Optional<EntradaMercancia> findById(Long id) {
        return jpaRepository.findById(id).map(EntradaMercanciaMapper::ToDomain);
    }

    @Override
    public List<EntradaMercancia> findAll() {
        return jpaRepository.findAll().stream().map(EntradaMercanciaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<EntradaMercancia> findByOrdenCompraId(Long ordenCompraId) {
        return jpaRepository.findByOrdenCompra_Id(ordenCompraId).stream().map(EntradaMercanciaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByOrdenCompraId(Long ordenCompraId) {
        return jpaRepository.existsByOrdenCompra_Id(ordenCompraId);
    }
}
