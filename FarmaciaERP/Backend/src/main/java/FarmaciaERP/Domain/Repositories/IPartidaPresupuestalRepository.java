package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.PartidaPresupuestal;

import java.util.List;
import java.util.Optional;

public interface IPartidaPresupuestalRepository {

    PartidaPresupuestal save(PartidaPresupuestal partida);

    Optional<PartidaPresupuestal> findById(Long id);

    List<PartidaPresupuestal> findAll();

    List<PartidaPresupuestal> findByCentroCostoId(Long centroCostoId);

    boolean existsByCodigo(String codigo);
}