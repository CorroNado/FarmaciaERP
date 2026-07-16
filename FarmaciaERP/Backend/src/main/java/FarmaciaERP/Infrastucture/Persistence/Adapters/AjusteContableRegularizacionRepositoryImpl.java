package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.AjusteContableRegularizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DisputaComercialJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.AjusteContableRegularizacionMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IAjusteContableRegularizacionJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IDisputaComercialJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AjusteContableRegularizacionRepositoryImpl implements IAjusteContableRegularizacionRepository {

    private final IAjusteContableRegularizacionJPARepository jpaRepository;
    private final IDisputaComercialJPARepository disputaComercialJPARepository;

    public AjusteContableRegularizacionRepositoryImpl(IAjusteContableRegularizacionJPARepository jpaRepository,
                                                        IDisputaComercialJPARepository disputaComercialJPARepository) {
        this.jpaRepository = jpaRepository;
        this.disputaComercialJPARepository = disputaComercialJPARepository;
    }

    @Override
    public AjusteContableRegularizacion save(AjusteContableRegularizacion ajusteContableRegularizacion) {
        DisputaComercialJPA disputaRef = disputaComercialJPARepository
                .findById(ajusteContableRegularizacion.getDisputaComercial().getId())
                .orElseThrow(() -> new BadRequestException("Disputa comercial no encontrada"));

        AjusteContableRegularizacionJPA entity = AjusteContableRegularizacionMapper.ToEntity(
                ajusteContableRegularizacion, disputaRef);
        AjusteContableRegularizacionJPA saved = jpaRepository.save(entity);
        return AjusteContableRegularizacionMapper.ToDomain(saved);
    }

    @Override
    public Optional<AjusteContableRegularizacion> findById(Long id) {
        return jpaRepository.findById(id).map(AjusteContableRegularizacionMapper::ToDomain);
    }

    @Override
    public List<AjusteContableRegularizacion> findAll() {
        return jpaRepository.findAll().stream().map(AjusteContableRegularizacionMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<AjusteContableRegularizacion> findByDisputaComercialId(Long disputaComercialId) {
        return jpaRepository.findByDisputaComercial_Id(disputaComercialId).stream()
                .map(AjusteContableRegularizacionMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByDisputaComercialId(Long disputaComercialId) {
        return jpaRepository.existsByDisputaComercial_Id(disputaComercialId);
    }
}
