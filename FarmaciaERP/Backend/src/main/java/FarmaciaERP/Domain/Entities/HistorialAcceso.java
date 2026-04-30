package FarmaciaERP.Domain.Entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistorialAcceso {

    private Long id;
    private Long usuarioId;
    private String email;
    private String accion;
    private String ip;
    private LocalDateTime fecha;

    public HistorialAcceso() {}

    public HistorialAcceso(
            Long usuarioId,
            String email,
            String accion,
            String ip) {

        this.usuarioId = usuarioId;
        this.email = email;
        this.accion = accion;
        this.ip = ip;
        this.fecha = LocalDateTime.now();
    }
}