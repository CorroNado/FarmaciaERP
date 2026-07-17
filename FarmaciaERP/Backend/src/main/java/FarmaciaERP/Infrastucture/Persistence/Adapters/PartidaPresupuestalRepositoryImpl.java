package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.PartidaPresupuestal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPartidaPresupuestalRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PartidaPresupuestalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.PartidaPresupuestalMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICentroCostoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPartidaPresupuestalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PartidaPresupuestalRepositoryImpl implements IPartidaPresupuestalRepository {

    private final IPartidaPresupuestalJPARepository jpaRepository;
    private final ICentroCostoJPARepository centroCostoJPARepository;

    public PartidaPresupuestalRepositoryImpl(IPartidaPresupuestalJPARepository jpaRepository,
                                              ICentroCostoJPARepository centroCostoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.centroCostoJPARepository = centroCostoJPARepository;
    }

    @Override
    public PartidaPresupuestal save(PartidaPresupuestal partida) {
        CentroCostoJPA centroCostoRef = centroCostoJPARepository.findById(partida.getCentroCosto().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Centro de costo no encontrado: " + partida.getCentroCosto().getId()));

        PartidaPresupuestalJPA entity = PartidaPresupuestalMapper.ToEntity(partida, centroCostoRef);
        PartidaPresupuestalJPA saved = jpaRepository.save(entity);
        return PartidaPresupuestalMapper.ToDomain(saved);
    }

    @Override
    public Optional<PartidaPresupuestal> findById(Long id) {
        return jpaRepository.findById(id).map(PartidaPresupuestalMapper::ToDomain);
    }

    @Override
    public List<PartidaPresupuestal> findAll() {
        return jpaRepository.findAll().stream().map(PartidaPresupuestalMapper::ToDomain).toList();
    }

    @Override
    public List<PartidaPresupuestal> findByCentroCostoId(Long centroCostoId) {
        return jpaRepository.findByCentroCosto_Id(centroCostoId).stream()
                .map(PartidaPresupuestalMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}