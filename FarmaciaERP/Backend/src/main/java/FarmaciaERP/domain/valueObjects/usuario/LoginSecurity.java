package FarmaciaERP.domain.valueObjects.usuario;

import FarmaciaERP.domain.enums.UserStatus;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class LoginSecurity {
    private Integer intentosLogin;
    private LocalDateTime tiempoDesbloqueo;
    private UserStatus estado;

    public LoginSecurity(Integer intentosLogin, LocalDateTime tiempoDesbloqueo, UserStatus estado) {
        if (intentosLogin < 0)
            throw new IllegalArgumentException("Intentos no puede ser negativo");
        this.intentosLogin = intentosLogin;
        this.tiempoDesbloqueo = tiempoDesbloqueo;
        this.estado = estado;
    }
}
