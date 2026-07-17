package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TurnoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramarAsistenciaRequest {
    private Long empleadoId;
    private LocalDate fecha;
    private TurnoAsistencia turno;
}
