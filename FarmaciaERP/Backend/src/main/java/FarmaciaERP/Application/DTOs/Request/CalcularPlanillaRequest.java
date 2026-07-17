package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalcularPlanillaRequest {
    /** 1 = Enero ... 12 = Diciembre. */
    private int mes;
    private int anio;
}
