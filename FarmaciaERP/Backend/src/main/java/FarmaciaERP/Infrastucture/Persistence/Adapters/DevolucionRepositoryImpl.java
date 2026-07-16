package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Devolucion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDevolucionRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.DevolucionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.VentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.DevolucionMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IDevolucionJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IMedicamentoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IVentaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DevolucionRepositoryImpl implements IDevolucionRepository {

    private final IDevolucionJPARepository jpaRepository;
    private final IVentaJPARepository ventaJPARepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;

    public DevolucionRepositoryImpl(IDevolucionJPARepository jpaRepository,
                                     IVentaJPARepository ventaJPARepository,
                                     IMedicamentoJPARepository medicamentoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.ventaJPARepository = ventaJPARepository;
        this.medicamentoJPARepository = medicamentoJPARepository;
    }

    @Override
    public Devolucion save(Devolucion devolucion) {
        VentaJPA ventaRef = ventaJPARepository.findById(devolucion.getVenta().getId())
                .orElseThrow(() -> new BadRequestException("Venta no encontrada: " + devolucion.getVenta().getId()));

        Map<Integer, MedicamentoJPA> medicamentosRef = devolucion.getDetalles().stream()
                .map(detalle -> detalle.getMedicamento().getId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> medicamentoJPARepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + id))
                ));

        DevolucionJPA entity = DevolucionMapper.ToEntity(devolucion, ventaRef, medicamentosRef);
        DevolucionJPA saved = jpaRepository.save(entity);
        return DevolucionMapper.ToDomain(saved);
    }

    @Override
    public Optional<Devolucion> findById(Long id) {
        return jpaRepository.findById(id)
                .map(DevolucionMapper::ToDomain);
    }

    @Override
    public List<Devolucion> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(DevolucionMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Devolucion> findByVentaId(Long ventaId) {
        return jpaRepository.findByVenta_Id(ventaId)
                .stream()
                .map(DevolucionMapper::ToDomain)
                .toList();
    }
}
