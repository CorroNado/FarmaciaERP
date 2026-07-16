package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearPartidaPresupuestalRequest {
    private String codigo;
    private String nombre;
    private Long centroCostoId;
    private BigDecimal montoPresupuestado;
}