package FarmaciaERP.infrastucture.persistence.embeddable.usuario;

import FarmaciaERP.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class LoginSecurityEmb {

    @Column(name = "login_attempts", nullable = false)
    private Integer loginAttempts;

    @Column(name = "lock_until")
    private LocalDateTime lockUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private UserStatus estado;

    protected LoginSecurityEmb() {
    }

    public LoginSecurityEmb(
            Integer loginAttempts,
            LocalDateTime lockUntil,
            UserStatus estado
    ) {
        this.loginAttempts = loginAttempts;
        this.lockUntil = lockUntil;
        this.estado = estado;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }

    public UserStatus getEstado() {
        return estado;
    }
}