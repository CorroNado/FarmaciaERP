package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Enums.EstadoCotizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICotizacionRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CotizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CotizacionMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IClienteJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICotizacionJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IMedicamentoJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CotizacionRepositoryImpl implements ICotizacionRepository {

    private final ICotizacionJPARepository jpaRepository;
    private final IClienteJPARepository clienteJPARepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;

    public CotizacionRepositoryImpl(ICotizacionJPARepository jpaRepository,
                                     IClienteJPARepository clienteJPARepository,
                                     IMedicamentoJPARepository medicamentoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.clienteJPARepository = clienteJPARepository;
        this.medicamentoJPARepository = medicamentoJPARepository;
    }

    @Override
    public Cotizacion save(Cotizacion cotizacion) {
        ClienteJPA clienteRef = clienteJPARepository.findById(cotizacion.getCliente().getId())
                .orElseThrow(() -> new BadRequestException("Cliente no encontrado: " + cotizacion.getCliente().getId()));

        Map<Integer, MedicamentoJPA> medicamentosRef = cotizacion.getDetalles().stream()
                .map(detalle -> detalle.getMedicamento().getId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> medicamentoJPARepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + id))
                ));

        CotizacionJPA entity = CotizacionMapper.ToEntity(cotizacion, clienteRef, medicamentosRef);
        CotizacionJPA saved = jpaRepository.save(entity);
        return CotizacionMapper.ToDomain(saved);
    }

    @Override
    public Optional<Cotizacion> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CotizacionMapper::ToDomain);
    }

    @Override
    public List<Cotizacion> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(CotizacionMapper::ToDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Cotizacion> findByClienteId(Long clienteId) {
        return jpaRepository.findByCliente_Id(clienteId)
                .stream()
                .map(CotizacionMapper::ToDomain)
                .toList();
    }

    @Override
    public List<Cotizacion> findByEstado(EstadoCotizacion estado) {
        return jpaRepository.findByEstado(estado)
                .stream()
                .map(CotizacionMapper::ToDomain)
                .toList();
    }
}
