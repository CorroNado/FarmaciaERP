package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDispersionBancariaCierreRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.DispersionBancariaCierreJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PropuestaPagoAutomaticaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.DispersionBancariaCierreMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IDispersionBancariaCierreJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPropuestaPagoAutomaticaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DispersionBancariaCierreRepositoryImpl implements IDispersionBancariaCierreRepository {

    private final IDispersionBancariaCierreJPARepository jpaRepository;
    private final IPropuestaPagoAutomaticaJPARepository propuestaPagoAutomaticaJPARepository;

    public DispersionBancariaCierreRepositoryImpl(IDispersionBancariaCierreJPARepository jpaRepository,
                                                   IPropuestaPagoAutomaticaJPARepository propuestaPagoAutomaticaJPARepository) {
        this.jpaRepository = jpaRepository;
        this.propuestaPagoAutomaticaJPARepository = propuestaPagoAutomaticaJPARepository;
    }

    @Override
    public DispersionBancariaCierre save(DispersionBancariaCierre dispersionBancariaCierre) {
        PropuestaPagoAutomaticaJPA propuestaRef = propuestaPagoAutomaticaJPARepository
                .findById(dispersionBancariaCierre.getPropuestaPagoAutomatica().getId())
                .orElseThrow(() -> new BadRequestException(
                        "La propuesta de pago de la dispersión bancaria no fue encontrada: "
                                + dispersionBancariaCierre.getPropuestaPagoAutomatica().getId()));

        DispersionBancariaCierreJPA entity = DispersionBancariaCierreMapper.ToEntity(dispersionBancariaCierre, propuestaRef);
        DispersionBancariaCierreJPA saved = jpaRepository.save(entity);
        return DispersionBancariaCierreMapper.ToDomain(saved);
    }

    @Override
    public Optional<DispersionBancariaCierre> findById(Long id) {
        return jpaRepository.findById(id).map(DispersionBancariaCierreMapper::ToDomain);
    }

    @Override
    public List<DispersionBancariaCierre> findAll() {
        return jpaRepository.findAll().stream().map(DispersionBancariaCierreMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByPropuestaPagoAutomaticaId(Long propuestaPagoAutomaticaId) {
        return jpaRepository.existsByPropuestaPagoAutomatica_Id(propuestaPagoAutomaticaId);
    }
}
