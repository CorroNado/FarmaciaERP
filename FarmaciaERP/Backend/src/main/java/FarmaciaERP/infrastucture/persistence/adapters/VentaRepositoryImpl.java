package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Venta;
import FarmaciaERP.domain.enums.EstadoVenta;
import FarmaciaERP.domain.exceptions.BadRequestException;
import FarmaciaERP.domain.repositories.IVentaRepository;
import FarmaciaERP.infrastucture.persistence.entities.ClienteJPA;
import FarmaciaERP.infrastucture.persistence.entities.MedicamentoJPA;
import FarmaciaERP.infrastucture.persistence.entities.VentaJPA;
import FarmaciaERP.infrastucture.persistence.mappers.VentaMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IClienteJPARepository;
import FarmaciaERP.infrastucture.persistence.repositories.IMedicamentoJPARepository;
import FarmaciaERP.infrastucture.persistence.repositories.IVentaJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VentaRepositoryImpl implements IVentaRepository {

    private final IVentaJPARepository jpaRepository;
    private final IClienteJPARepository clienteJPARepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;
    private final VentaMapper ventaMapper;

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

        VentaJPA entity = ventaMapper.toEntity(venta, clienteRef, medicamentosRef);
        VentaJPA saved = jpaRepository.save(entity);
        return ventaMapper.toDomain(saved);
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ventaMapper::toDomain);
    }

    @Override
    public List<Venta> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ventaMapper::toDomain)
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
                .map(ventaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Venta> findByEstado(EstadoVenta estado) {
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(ventaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Venta> findByFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
        return jpaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(ventaMapper::toDomain)
                .toList();
    }
}
