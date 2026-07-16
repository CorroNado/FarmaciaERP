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
public class LineaAsientoResponse {
    private Long id;
    private Long subcuentaId;
    private String subcuentaCodigo;
    private String subcuentaNombre;
    private Long centroCostoId;
    private String centroCostoCodigo;
    private String centroCostoNombre;
    private BigDecimal debe;
    private BigDecimal haber;
    private String glosaDetalle;
}