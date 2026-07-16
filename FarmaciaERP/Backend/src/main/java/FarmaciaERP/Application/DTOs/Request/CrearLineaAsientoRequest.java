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
public class CrearLineaAsientoRequest {
    private Long subcuentaId;
    private Long centroCostoId;
    private BigDecimal debe;
    private BigDecimal haber;
    private String glosaDetalle;
}