package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.EstadoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditarRegistroAsistenciaRequest {
    private String usuario;
    private LocalTime horaEntrada;
    private EstadoAsistencia estado;
    private String motivoAuditoria;
}
