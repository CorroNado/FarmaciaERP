package FarmaciaERP.domain.valueObjects.usuario;

import FarmaciaERP.domain.enums.UserStatus;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class LoginSecurity {
    Integer intentosLogin;
    LocalDateTime tiempoDesbloqueo;
    UserStatus estado;

    public LoginSecurity() {
        this.intentosLogin = 0;
        this.tiempoDesbloqueo = null;
        this.estado = UserStatus.ACTIVO;
    }

    public LoginSecurity(Integer intentosLogin) {
        this.intentosLogin = intentosLogin;
        this.tiempoDesbloqueo = null;
        this.estado = UserStatus.ACTIVO;
    }

    public LoginSecurity(UserStatus estado) {
        this.intentosLogin = 0;
        this.tiempoDesbloqueo = null;
        this.estado = estado;
    }

    public LoginSecurity(Integer intentosLogin, LocalDateTime tiempoDesbloqueo, UserStatus estado) {
        if (intentosLogin < 0)
            throw new IllegalArgumentException("Intentos no puede ser negativo");
        this.intentosLogin = intentosLogin;
        this.tiempoDesbloqueo = tiempoDesbloqueo;
        this.estado = estado;
    }
}
