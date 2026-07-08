package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.VentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.VentaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IClienteJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IMedicamentoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IVentaJPARepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VentaRepositoryImpl implements IVentaRepository {

    private final IVentaJPARepository jpaRepository;
    private final IClienteJPARepository clienteJPARepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;

    public VentaRepositoryImpl(IVentaJPARepository jpaRepository,
                                IClienteJPARepository clienteJPARepository,
                                IMedicamentoJPARepository medicamentoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.clienteJPARepository = clienteJPARepository;
        this.medicamentoJPARepository = medicamentoJPARepository;
    }

    @Override
    public Venta save(Venta venta) {
        ClienteJPA clienteRef = clienteJPARepository.findById(venta.getCliente().getId())
                .orElseThrow(() -> new BadRequestException("Cliente no encontrado: " + venta.getCliente().getId()));

        Map<Integer, MedicamentoJPA> medicamentosRef = venta.getDetalles().stream()
                .map(detalle -> detalle.getMedicamento().getId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> medicamentoJPARepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + id))
                ));

        VentaJPA entity = VentaMapper.ToEntity(venta, clienteRef, medicamentosRef);
        VentaJPA saved = jpaRepository.save(entity);
        return VentaMapper.ToDomain(saved);
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return jpaRepository.findById(id)
                .map(VentaMapper::ToDomain);
    }

    @Override
    public List<Venta> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(VentaMapper::ToDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Venta> findByClienteId(Long clienteId) {
        return jpaRepository.findByCliente_Id(clienteId)
                .stream()
                .map(VentaMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Venta> findByEstado(EstadoVenta estado) {
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(VentaMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Venta> findByFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return jpaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(VentaMapper::ToDomain)
                .toList();
    }
}
