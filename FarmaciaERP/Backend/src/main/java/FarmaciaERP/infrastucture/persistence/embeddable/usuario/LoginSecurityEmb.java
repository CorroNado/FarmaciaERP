package FarmaciaERP.infrastucture.persistence.embeddable.usuario;

import FarmaciaERP.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class LoginSecurityEmb {

    @Column(name = "login_attempts", nullable = false)
    private Integer intentosLogin;

    @Column(name = "lock_until")
    private LocalDateTime tiempoDesbloqueo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private UserStatus estado;

    protected LoginSecurityEmb() {
    }

    public LoginSecurityEmb(
            Integer intentosLogin,
            LocalDateTime tiempoDesbloqueo,
            UserStatus estado
    ) {
        this.intentosLogin = intentosLogin;
        this.tiempoDesbloqueo = tiempoDesbloqueo;
        this.estado = estado;
    }
}