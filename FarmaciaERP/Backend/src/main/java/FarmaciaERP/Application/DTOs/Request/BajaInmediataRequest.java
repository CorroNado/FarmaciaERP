package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BajaInmediataRequest {
    private String usuario;
    private String motivo;
    // Información del turno que se cancela (proviene del módulo de Asistencia)
    private String turnoInfo;
}
