package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.RolEmpleado;
import FarmaciaERP.Domain.Enums.TipoContrato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarEmpleadoRequest {
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String dni;
    private RolEmpleado rol;
    private String area;
    private LocalDate fechaIngreso;
    private double salario;
    private TipoContrato contrato;
    private String correo;
    private String telefono;
}
