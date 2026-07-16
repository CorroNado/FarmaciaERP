package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.AjusteContableRegularizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.LotePagoTesoreriaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.LotePagoTesoreriaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IAjusteContableRegularizacionJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ILotePagoTesoreriaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LotePagoTesoreriaRepositoryImpl implements ILotePagoTesoreriaRepository {

    private final ILotePagoTesoreriaJPARepository jpaRepository;
    private final IAjusteContableRegularizacionJPARepository ajusteContableRegularizacionJPARepository;

    public LotePagoTesoreriaRepositoryImpl(ILotePagoTesoreriaJPARepository jpaRepository,
                                            IAjusteContableRegularizacionJPARepository ajusteContableRegularizacionJPARepository) {
        this.jpaRepository = jpaRepository;
        this.ajusteContableRegularizacionJPARepository = ajusteContableRegularizacionJPARepository;
    }

    @Override
    public LotePagoTesoreria save(LotePagoTesoreria lotePagoTesoreria) {
        List<Long> ids = lotePagoTesoreria.getAjustesContables().stream()
                .map(ajuste -> ajuste.getId()).toList();
        List<AjusteContableRegularizacionJPA> ajustesRef = ajusteContableRegularizacionJPARepository.findAllById(ids);
        if (ajustesRef.size() != ids.size()) {
            throw new BadRequestException("Uno o más ajustes contables del lote de pagos no fueron encontrados");
        }

        LotePagoTesoreriaJPA entity = LotePagoTesoreriaMapper.ToEntity(lotePagoTesoreria, ajustesRef);
        LotePagoTesoreriaJPA saved = jpaRepository.save(entity);
        return LotePagoTesoreriaMapper.ToDomain(saved);
    }

    @Override
    public Optional<LotePagoTesoreria> findById(Long id) {
        return jpaRepository.findById(id).map(LotePagoTesoreriaMapper::ToDomain);
    }

    @Override
    public List<LotePagoTesoreria> findAll() {
        return jpaRepository.findAll().stream().map(LotePagoTesoreriaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByAjusteContableRegularizacionId(Long ajusteContableId) {
        return jpaRepository.existsByAjusteContableRegularizacionId(ajusteContableId);
    }
}
