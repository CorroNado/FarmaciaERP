package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.LoginHistory;
import FarmaciaERP.infrastucture.persistence.entities.LoginHistoryJPA;
import FarmaciaERP.infrastucture.persistence.entities.UserJPA;
import org.springframework.stereotype.Component;

@Component
public class LoginHistoryMapper {
    private LoginHistoryMapper() {}

    public LoginHistoryJPA toEntity(LoginHistory domain) {
        UserJPA usuarioRef = new UserJPA();
        usuarioRef.setUserId(domain.getUsuarioId());
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

    public LoginHistory toDomain(LoginHistoryJPA entity) {
        return new LoginHistory(
                entity.getLoginHistorialId(),
                entity.getUsuario().getUserId(),
                entity.getAccion(),
                entity.getIp(),
                entity.getUserAgent(),
                entity.getFecha()
        );
    }
}
