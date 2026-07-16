package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.DisputaComercialJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ExcepcionFacturacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.DisputaComercialMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IDisputaComercialJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IExcepcionFacturacionJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DisputaComercialRepositoryImpl implements IDisputaComercialRepository {

    private final IDisputaComercialJPARepository jpaRepository;
    private final IExcepcionFacturacionJPARepository excepcionFacturacionJPARepository;

    public DisputaComercialRepositoryImpl(IDisputaComercialJPARepository jpaRepository,
                                           IExcepcionFacturacionJPARepository excepcionFacturacionJPARepository) {
        this.jpaRepository = jpaRepository;
        this.excepcionFacturacionJPARepository = excepcionFacturacionJPARepository;
    }

    @Override
    public DisputaComercial save(DisputaComercial disputaComercial) {
        ExcepcionFacturacionJPA excepcionRef = excepcionFacturacionJPARepository
                .findById(disputaComercial.getExcepcionFacturacion().getId())
                .orElseThrow(() -> new BadRequestException("Excepción de facturación no encontrada"));

        DisputaComercialJPA entity = DisputaComercialMapper.ToEntity(disputaComercial, excepcionRef);
        DisputaComercialJPA saved = jpaRepository.save(entity);
        return DisputaComercialMapper.ToDomain(saved);
    }

    @Override
    public Optional<DisputaComercial> findById(Long id) {
        return jpaRepository.findById(id).map(DisputaComercialMapper::ToDomain);
    }

    @Override
    public List<DisputaComercial> findAll() {
        return jpaRepository.findAll().stream().map(DisputaComercialMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<DisputaComercial> findByExcepcionFacturacionId(Long excepcionFacturacionId) {
        return jpaRepository.findByExcepcionFacturacion_Id(excepcionFacturacionId).stream()
                .map(DisputaComercialMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByExcepcionFacturacionId(Long excepcionFacturacionId) {
        return jpaRepository.existsByExcepcionFacturacion_Id(excepcionFacturacionId);
    }
}
