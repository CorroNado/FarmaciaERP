package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Domain.Enums.EstadoSolPed;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConvenioJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SolicitudPedidoJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.SolicitudPedidoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IConvenioJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IMedicamentoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IProveedorJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISolicitudPedidoJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SolicitudPedidoRepositoryImpl implements ISolicitudPedidoRepository {

    private final ISolicitudPedidoJPARepository jpaRepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;
    private final IProveedorJPARepository proveedorJPARepository;
    private final IConvenioJPARepository convenioJPARepository;

    public SolicitudPedidoRepositoryImpl(ISolicitudPedidoJPARepository jpaRepository,
                                          IMedicamentoJPARepository medicamentoJPARepository,
                                          IProveedorJPARepository proveedorJPARepository,
                                          IConvenioJPARepository convenioJPARepository) {
        this.jpaRepository = jpaRepository;
        this.medicamentoJPARepository = medicamentoJPARepository;
        this.proveedorJPARepository = proveedorJPARepository;
        this.convenioJPARepository = convenioJPARepository;
    }

    @Override
    public SolicitudPedido save(SolicitudPedido solicitudPedido) {
        Map<Integer, MedicamentoJPA> medicamentosRef = solicitudPedido.getDetalles().stream()
                .map(detalle -> detalle.getMedicamento().getId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> medicamentoJPARepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + id))
                ));

        ProveedorJPA proveedorRef = null;
        if (solicitudPedido.getProveedor() != null) {
            proveedorRef = proveedorJPARepository.findById(solicitudPedido.getProveedor().getId())
                    .orElseThrow(() -> new BadRequestException("Proveedor no encontrado"));
        }

        ConvenioJPA convenioRef = null;
        if (solicitudPedido.getConvenio() != null) {
            convenioRef = convenioJPARepository.findById(solicitudPedido.getConvenio().getId())
                    .orElseThrow(() -> new BadRequestException("Convenio no encontrado"));
        }

        SolicitudPedidoJPA entity = SolicitudPedidoMapper.ToEntity(solicitudPedido, medicamentosRef, proveedorRef, convenioRef);
        SolicitudPedidoJPA saved = jpaRepository.save(entity);
        return SolicitudPedidoMapper.ToDomain(saved);
    }

    @Override
    public Optional<SolicitudPedido> findById(Long id) {
        return jpaRepository.findById(id).map(SolicitudPedidoMapper::ToDomain);
    }

    @Override
    public List<SolicitudPedido> findAll() {
        return jpaRepository.findAll().stream().map(SolicitudPedidoMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<SolicitudPedido> findByEstado(EstadoSolPed estado) {
        return jpaRepository.findByEstado(estado).stream().map(SolicitudPedidoMapper::ToDomain).toList();
    }
}
