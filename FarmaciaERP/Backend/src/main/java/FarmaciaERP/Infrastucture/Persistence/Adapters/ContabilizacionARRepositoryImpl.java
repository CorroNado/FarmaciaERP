package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CierreCajaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ContabilizacionARMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICierreCajaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IContabilizacionARJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ContabilizacionARRepositoryImpl implements IContabilizacionARRepository {

    private final IContabilizacionARJPARepository jpaRepository;
    private final ICierreCajaJPARepository cierreCajaJPARepository;

    public ContabilizacionARRepositoryImpl(IContabilizacionARJPARepository jpaRepository,
                                            ICierreCajaJPARepository cierreCajaJPARepository) {
        this.jpaRepository = jpaRepository;
        this.cierreCajaJPARepository = cierreCajaJPARepository;
    }

    @Override
    public ContabilizacionAR save(ContabilizacionAR contabilizacion) {
        CierreCajaJPA cierreCajaRef = cierreCajaJPARepository.findById(contabilizacion.getCierreCaja().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Cierre de caja no encontrado: " + contabilizacion.getCierreCaja().getId()));

        ContabilizacionARJPA entity = ContabilizacionARMapper.ToEntity(contabilizacion, cierreCajaRef);
        ContabilizacionARJPA saved = jpaRepository.save(entity);
        return ContabilizacionARMapper.ToDomain(saved);
    }

    @Override
    public Optional<ContabilizacionAR> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ContabilizacionARMapper::ToDomain);
    }

    @Override
    public Optional<ContabilizacionAR> findByCierreCajaId(Long cierreCajaId) {
        return jpaRepository.findByCierreCaja_Id(cierreCajaId)
                .map(ContabilizacionARMapper::ToDomain);
    }

    @Override
    public List<ContabilizacionAR> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ContabilizacionARMapper::ToDomain)
                .toList();
    }
}
