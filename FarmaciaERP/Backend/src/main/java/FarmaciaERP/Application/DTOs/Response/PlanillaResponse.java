package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanillaResponse {
    private Long id;
    private int mes;
    private String nombreMes;
    private int anio;
    private boolean guardada;
    private List<DetallePlanillaEmpleadoResponse> detalles;
    private double montoTotalNeto;
    private LocalDateTime fechaCalculo;
    private LocalDateTime fechaGuardado;
}
