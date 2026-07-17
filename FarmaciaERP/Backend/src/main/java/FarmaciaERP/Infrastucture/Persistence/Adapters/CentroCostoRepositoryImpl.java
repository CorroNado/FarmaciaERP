package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CentroCostoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICentroCostoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISucursalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CentroCostoRepositoryImpl implements ICentroCostoRepository {

    private final ICentroCostoJPARepository jpaRepository;
    private final ISucursalJPARepository sucursalJPARepository;

    public CentroCostoRepositoryImpl(ICentroCostoJPARepository jpaRepository,
                                      ISucursalJPARepository sucursalJPARepository) {
        this.jpaRepository = jpaRepository;
        this.sucursalJPARepository = sucursalJPARepository;
    }

    @Override
    public CentroCosto save(CentroCosto centroCosto) {
        SucursalJPA sucursalRef = null;
        if (centroCosto.getSucursal() != null) {
            sucursalRef = sucursalJPARepository.findById(centroCosto.getSucursal().getId())
                    .orElseThrow(() -> new BadRequestException(
                            "Sucursal no encontrada: " + centroCosto.getSucursal().getId()));
        }

        CentroCostoJPA entity = CentroCostoMapper.ToEntity(centroCosto, sucursalRef);
        CentroCostoJPA saved = jpaRepository.save(entity);
        return CentroCostoMapper.ToDomain(saved);
    }

    @Override
    public Optional<CentroCosto> findById(Long id) {
        return jpaRepository.findById(id).map(CentroCostoMapper::ToDomain);
    }

    @Override
    public Optional<CentroCosto> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(CentroCostoMapper::ToDomain);
    }

    @Override
    public List<CentroCosto> findAll() {
        return jpaRepository.findAll().stream().map(CentroCostoMapper::ToDomain).toList();
    }

    @Override
    public List<CentroCosto> findAllActivos() {
        return jpaRepository.findByActivoTrue().stream().map(CentroCostoMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}