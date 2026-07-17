package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Enums.RolEmpleado;
import FarmaciaERP.Domain.Enums.TipoContrato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoResponse {
    private Long id;
    private String codigo;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String nombreCompleto;
    private String dni;
    private RolEmpleado rol;
    private String area;
    private LocalDate fechaIngreso;
    private double salario;
    private TipoContrato contrato;
    private String correo;
    private String telefono;
    private EstadoEmpleado estado;
    private LocalDateTime bajaProgramadaFechaEfectiva;
    private String bajaProgramadaObservacion;
    private String bajaProgramadaTurnoInfo;
}
