package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.LoginAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class LoginHistory {

    private Long id;
    private Long usuarioId;
    private LoginAction accion;
    private String userAgent;
    private String ip;
    private LocalDateTime fecha;


    public LoginHistory() {}

    public LoginHistory(Long usuarioId, LoginAction accion, String ip,
                        String userAgent) {
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.ip = ip;
        this.userAgent = userAgent;
        this.fecha = LocalDateTime.now();
    }
}