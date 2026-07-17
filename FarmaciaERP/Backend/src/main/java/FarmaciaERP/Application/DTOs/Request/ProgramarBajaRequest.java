package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramarBajaRequest {
    private String usuario;
    private LocalDateTime fechaEfectiva;
    private String observacion;
    // Información del último turno activo (proviene del módulo de Asistencia)
    private String turnoInfo;
}
