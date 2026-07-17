package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanillaResumenResponse {
    private Long id;
    private int mes;
    private String nombreMes;
    private int anio;
    private int cantidadColaboradores;
    private double montoTotalNeto;
    private LocalDateTime fechaGuardado;
}
