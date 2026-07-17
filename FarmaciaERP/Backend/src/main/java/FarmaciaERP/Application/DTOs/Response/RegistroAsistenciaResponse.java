package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoAsistencia;
import FarmaciaERP.Domain.Enums.TurnoAsistencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroAsistenciaResponse {
    private Long id;
    private Long empleadoId;
    private String codigoEmpleado;
    private String colaborador;
    private String rol;
    private LocalDate fecha;
    private TurnoAsistencia turno;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private Double horasTrabajadas;
    private Double horasExtras;
    private Double factorExtra;
    private EstadoAsistencia estado;
    private boolean registrado;
    private boolean justificado;
    private String motivoJustificacion;
    private LocalDateTime fechaCreacion;
}
