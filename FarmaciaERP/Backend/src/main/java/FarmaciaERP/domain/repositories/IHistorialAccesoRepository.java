package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.LoginHistory;

import java.util.List;

public interface IHistorialAccesoRepository {

    LoginHistory save(LoginHistory historial);

    List<LoginHistory> findAll();

    List<LoginHistory> findByUsuarioId(Long usuarioId);
}