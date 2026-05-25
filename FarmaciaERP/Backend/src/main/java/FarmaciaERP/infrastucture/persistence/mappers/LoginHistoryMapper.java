package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.LoginHistory;
import FarmaciaERP.infrastucture.persistence.entities.LoginHistoryJPA;
import FarmaciaERP.infrastucture.persistence.entities.UserJPA;

public class LoginHistoryMapper {
    private LoginHistoryMapper() {}

    public static LoginHistoryJPA toEntity(LoginHistory domain) {
        UserJPA usuarioRef = new UserJPA();
        usuarioRef.setId(domain.getUsuarioId());
        LoginHistoryJPA entity = new LoginHistoryJPA(
                domain.getId(),
                usuarioRef,
                domain.getUserAgent(),
                domain.getAccion(),
                domain.getIp(),
                domain.getFecha()
        );
        return entity;
    }

    public static LoginHistory toDomain(LoginHistoryJPA entity) {
        return new LoginHistory(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getAccion(),
                entity.getIp(),
                entity.getUserAgent(),
                entity.getFecha()
        );
    }
}
