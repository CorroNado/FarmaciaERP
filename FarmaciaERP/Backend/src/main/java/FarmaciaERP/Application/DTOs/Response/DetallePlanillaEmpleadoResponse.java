package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetallePlanillaEmpleadoResponse {
    private Long empleadoId;
    private String codigoEmpleado;
    private String nombreCompleto;
    private double sueldoBase;
    private double sueldoDiario;
    private int diasConTurno;
    private int faltas;
    private int minutosTardanza;
    private double horasExtras;
    private double montoExtra;
    private double descuentoFaltas;
    private double descuentoTardanzas;
    private double bonoAsistencia;
    private double bonoCumplimiento;
    private double bonoMetas;
    private double essalud;
    private double afp;
    private double otrosDescuentos;
    private double sueldoNeto;
}
