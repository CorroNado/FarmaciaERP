package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CierreCajaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CierreCajaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICierreCajaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISucursalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CierreCajaRepositoryImpl implements ICierreCajaRepository {

    private final ICierreCajaJPARepository jpaRepository;
    private final ISucursalJPARepository sucursalJPARepository;

    public CierreCajaRepositoryImpl(ICierreCajaJPARepository jpaRepository,
                                     ISucursalJPARepository sucursalJPARepository) {
        this.jpaRepository = jpaRepository;
        this.sucursalJPARepository = sucursalJPARepository;
    }

    @Override
    public CierreCaja save(CierreCaja cierreCaja) {
        SucursalJPA sucursalRef = sucursalJPARepository.findById(cierreCaja.getSucursal().getId())
                .orElseThrow(() -> new BadRequestException("Sucursal no encontrada: " + cierreCaja.getSucursal().getId()));

        CierreCajaJPA entity = CierreCajaMapper.ToEntity(cierreCaja, sucursalRef);
        CierreCajaJPA saved = jpaRepository.save(entity);
        return CierreCajaMapper.ToDomain(saved);
    }

    @Override
    public Optional<CierreCaja> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CierreCajaMapper::ToDomain);
    }

    @Override
    public List<CierreCaja> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(CierreCajaMapper::ToDomain)
                .toList();
    }

    @Override
    public List<CierreCaja> findBySucursalId(Long sucursalId) {
        return jpaRepository.findBySucursal_Id(sucursalId)
                .stream()
                .map(CierreCajaMapper::ToDomain)
                .toList();
    }
}
