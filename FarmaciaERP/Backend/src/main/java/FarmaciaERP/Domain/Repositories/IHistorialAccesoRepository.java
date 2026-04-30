package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.HistorialAcceso;

import java.util.List;

public interface IHistorialAccesoRepository {

    HistorialAcceso save(HistorialAcceso historial);

    List<HistorialAcceso> findAll();

    List<HistorialAcceso> findByUsuarioId(Long usuarioId);
}