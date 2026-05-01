package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.ValueObjects.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class HistorialAcceso {

    private Long id;
    private Long usuarioId;
    private Email email;
    private String accion;
    private String ip;
    private LocalDateTime fecha;


    public HistorialAcceso() {}

    public HistorialAcceso(
            Long usuarioId,
            Email email,
            String accion,
            String ip) {

        this.usuarioId = usuarioId;
        this.email = email;
        this.accion = accion;
        this.ip = ip;
        this.fecha = LocalDateTime.now();
    }
}