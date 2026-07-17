package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.AsientoContableMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IAsientoContableJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICentroCostoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISubcuentaDivisionariaJPARepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AsientoContableRepositoryImpl implements IAsientoContableRepository {

    private final IAsientoContableJPARepository jpaRepository;
    private final ISubcuentaDivisionariaJPARepository subcuentaJPARepository;
    private final ICentroCostoJPARepository centroCostoJPARepository;

    public AsientoContableRepositoryImpl(IAsientoContableJPARepository jpaRepository,
                                         ISubcuentaDivisionariaJPARepository subcuentaJPARepository,
                                         ICentroCostoJPARepository centroCostoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.subcuentaJPARepository = subcuentaJPARepository;
        this.centroCostoJPARepository = centroCostoJPARepository;
    }

    @Override
    public AsientoContable save(AsientoContable asientoContable) {
        Map<Long, SubcuentaDivisionariaJPA> subcuentasMap = new HashMap<>();
        Map<Long, CentroCostoJPA> centrosCostoMap = new HashMap<>();

        for (LineaAsiento linea : asientoContable.getLineas()) {
            Long subcuentaId = linea.getSubcuenta().getId();
            if (!subcuentasMap.containsKey(subcuentaId)) {
                SubcuentaDivisionariaJPA subRef = subcuentaJPARepository.findById(subcuentaId)
                        .orElseThrow(() -> new BadRequestException("Subcuenta no encontrada: " + subcuentaId));
                subcuentasMap.put(subcuentaId, subRef);
            }

            if (linea.getCentroCosto() != null) {
                Long ccId = linea.getCentroCosto().getId();
                if (!centrosCostoMap.containsKey(ccId)) {
                    CentroCostoJPA ccRef = centroCostoJPARepository.findById(ccId)
                            .orElseThrow(() -> new BadRequestException("Centro de costo no encontrado: " + ccId));
                    centrosCostoMap.put(ccId, ccRef);
                }
            }
        }

        AsientoContableJPA entity = AsientoContableMapper.ToEntity(asientoContable, subcuentasMap, centrosCostoMap);
        AsientoContableJPA saved = jpaRepository.save(entity);
        return AsientoContableMapper.ToDomain(saved);
    }

    @Override
    public Optional<AsientoContable> findById(Long id) {
        return jpaRepository.findById(id).map(AsientoContableMapper::ToDomain);
    }

    @Override
    public Optional<AsientoContable> findByNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(AsientoContableMapper::ToDomain);
    }

    @Override
    public List<AsientoContable> findAll() {
        return jpaRepository.findAll().stream().map(AsientoContableMapper::ToDomain).toList();
    }

    @Override
    public List<AsientoContable> findByEstado(EstadoAsiento estado) {
        return jpaRepository.findByEstado(estado).stream().map(AsientoContableMapper::ToDomain).toList();
    }

    @Override
    public List<AsientoContable> findByFechaContableBetween(LocalDate inicio, LocalDate fin) {
        return jpaRepository.findByFechaContableBetween(inicio, fin).stream()
                .map(AsientoContableMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByNumero(String numero) {
        return jpaRepository.existsByNumero(numero);
    }
}