package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.OrdenTraslado;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IOrdenTrasladoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.InspeccionCalidadJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenTrasladoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.OrdenTrasladoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IInspeccionCalidadJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IOrdenTrasladoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISucursalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrdenTrasladoRepositoryImpl implements IOrdenTrasladoRepository {

    private final IOrdenTrasladoJPARepository jpaRepository;
    private final IInspeccionCalidadJPARepository inspeccionCalidadJPARepository;
    private final ISucursalJPARepository sucursalJPARepository;

    public OrdenTrasladoRepositoryImpl(IOrdenTrasladoJPARepository jpaRepository,
                                        IInspeccionCalidadJPARepository inspeccionCalidadJPARepository,
                                        ISucursalJPARepository sucursalJPARepository) {
        this.jpaRepository = jpaRepository;
        this.inspeccionCalidadJPARepository = inspeccionCalidadJPARepository;
        this.sucursalJPARepository = sucursalJPARepository;
    }

    @Override
    public OrdenTraslado save(OrdenTraslado ordenTraslado) {
        InspeccionCalidadJPA inspeccionCalidadRef = inspeccionCalidadJPARepository
                .findById(ordenTraslado.getInspeccionCalidad().getId())
                .orElseThrow(() -> new BadRequestException("Decisión de Empleo (QA11) no encontrada"));

        SucursalJPA sucursalRef = sucursalJPARepository
                .findById(ordenTraslado.getSucursalDestino().getId())
                .orElseThrow(() -> new BadRequestException("Sucursal destino no encontrada"));

        OrdenTrasladoJPA entity = OrdenTrasladoMapper.ToEntity(ordenTraslado, inspeccionCalidadRef, sucursalRef);
        OrdenTrasladoJPA saved = jpaRepository.save(entity);
        return OrdenTrasladoMapper.ToDomain(saved);
    }

    @Override
    public Optional<OrdenTraslado> findById(Long id) {
        return jpaRepository.findById(id).map(OrdenTrasladoMapper::ToDomain);
    }

    @Override
    public List<OrdenTraslado> findAll() {
        return jpaRepository.findAll().stream().map(OrdenTrasladoMapper::ToDomain).toList();
    }

    @Override
    public List<OrdenTraslado> findByInspeccionCalidadId(Long inspeccionCalidadId) {
        return jpaRepository.findByInspeccionCalidad_Id(inspeccionCalidadId).stream()
                .map(OrdenTrasladoMapper::ToDomain).toList();
    }

    @Override
    public List<OrdenTraslado> findBySucursalDestinoId(Long sucursalId) {
        return jpaRepository.findBySucursalDestino_Id(sucursalId).stream()
                .map(OrdenTrasladoMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByInspeccionCalidadId(Long inspeccionCalidadId) {
        return jpaRepository.existsByInspeccionCalidad_Id(inspeccionCalidadId);
    }
}
