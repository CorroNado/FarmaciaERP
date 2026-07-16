package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.*;
import FarmaciaERP.Infrastucture.Persistence.Mappers.OrdenCompraMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrdenCompraRepositoryImpl implements IOrdenCompraRepository {

    private final IOrdenCompraJPARepository jpaRepository;
    private final ISolicitudPedidoJPARepository solicitudPedidoJPARepository;
    private final IProveedorJPARepository proveedorJPARepository;
    private final IConvenioJPARepository convenioJPARepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;

    public OrdenCompraRepositoryImpl(IOrdenCompraJPARepository jpaRepository,
                                      ISolicitudPedidoJPARepository solicitudPedidoJPARepository,
                                      IProveedorJPARepository proveedorJPARepository,
                                      IConvenioJPARepository convenioJPARepository,
                                      IMedicamentoJPARepository medicamentoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.solicitudPedidoJPARepository = solicitudPedidoJPARepository;
        this.proveedorJPARepository = proveedorJPARepository;
        this.convenioJPARepository = convenioJPARepository;
        this.medicamentoJPARepository = medicamentoJPARepository;
    }

    @Override
    public OrdenCompra save(OrdenCompra ordenCompra) {
        SolicitudPedidoJPA solPedRef = solicitudPedidoJPARepository.findById(ordenCompra.getSolicitudPedido().getId())
                .orElseThrow(() -> new BadRequestException("SolPed no encontrada"));
        ProveedorJPA proveedorRef = proveedorJPARepository.findById(ordenCompra.getProveedor().getId())
                .orElseThrow(() -> new BadRequestException("Proveedor no encontrado"));
        ConvenioJPA convenioRef = convenioJPARepository.findById(ordenCompra.getConvenio().getId())
                .orElseThrow(() -> new BadRequestException("Convenio no encontrado"));

        Map<Integer, MedicamentoJPA> medicamentosRef = ordenCompra.getDetalles().stream()
                .map(detalle -> detalle.getMedicamento().getId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> medicamentoJPARepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + id))
                ));

        OrdenCompraJPA entity = OrdenCompraMapper.ToEntity(ordenCompra, solPedRef, proveedorRef, convenioRef, medicamentosRef);
        OrdenCompraJPA saved = jpaRepository.save(entity);
        return OrdenCompraMapper.ToDomain(saved);
    }

    @Override
    public Optional<OrdenCompra> findById(Long id) {
        return jpaRepository.findById(id).map(OrdenCompraMapper::ToDomain);
    }

    @Override
    public List<OrdenCompra> findAll() {
        return jpaRepository.findAll().stream().map(OrdenCompraMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<OrdenCompra> findBySolicitudPedidoId(Long solicitudPedidoId) {
        return jpaRepository.findBySolicitudPedido_Id(solicitudPedidoId).stream().map(OrdenCompraMapper::ToDomain).toList();
    }

    @Override
    public List<OrdenCompra> findByEstado(EstadoOrdenCompra estado) {
        return jpaRepository.findByEstado(estado).stream().map(OrdenCompraMapper::ToDomain).toList();
    }
}
