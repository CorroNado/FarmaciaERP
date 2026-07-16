package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartidaPresupuestalResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long centroCostoId;
    private BigDecimal montoPresupuestado;
    private boolean activa;
}