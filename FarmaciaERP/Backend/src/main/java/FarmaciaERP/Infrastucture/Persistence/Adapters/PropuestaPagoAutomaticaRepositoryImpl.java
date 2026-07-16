package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.LotePagoTesoreriaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PropuestaPagoAutomaticaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.PropuestaPagoAutomaticaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ILotePagoTesoreriaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPropuestaPagoAutomaticaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PropuestaPagoAutomaticaRepositoryImpl implements IPropuestaPagoAutomaticaRepository {

    private final IPropuestaPagoAutomaticaJPARepository jpaRepository;
    private final ILotePagoTesoreriaJPARepository lotePagoTesoreriaJPARepository;

    public PropuestaPagoAutomaticaRepositoryImpl(IPropuestaPagoAutomaticaJPARepository jpaRepository,
                                                  ILotePagoTesoreriaJPARepository lotePagoTesoreriaJPARepository) {
        this.jpaRepository = jpaRepository;
        this.lotePagoTesoreriaJPARepository = lotePagoTesoreriaJPARepository;
    }

    @Override
    public PropuestaPagoAutomatica save(PropuestaPagoAutomatica propuestaPagoAutomatica) {
        LotePagoTesoreriaJPA loteRef = lotePagoTesoreriaJPARepository
                .findById(propuestaPagoAutomatica.getLotePagoTesoreria().getId())
                .orElseThrow(() -> new BadRequestException(
                        "El lote de pagos de la propuesta no fue encontrado: "
                                + propuestaPagoAutomatica.getLotePagoTesoreria().getId()));

        PropuestaPagoAutomaticaJPA entity = PropuestaPagoAutomaticaMapper.ToEntity(propuestaPagoAutomatica, loteRef);
        PropuestaPagoAutomaticaJPA saved = jpaRepository.save(entity);
        return PropuestaPagoAutomaticaMapper.ToDomain(saved);
    }

    @Override
    public Optional<PropuestaPagoAutomatica> findById(Long id) {
        return jpaRepository.findById(id).map(PropuestaPagoAutomaticaMapper::ToDomain);
    }

    @Override
    public List<PropuestaPagoAutomatica> findAll() {
        return jpaRepository.findAll().stream().map(PropuestaPagoAutomaticaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByLotePagoTesoreriaId(Long lotePagoTesoreriaId) {
        return jpaRepository.existsByLotePagoTesoreria_Id(lotePagoTesoreriaId);
    }
}
