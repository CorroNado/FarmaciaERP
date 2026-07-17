package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsientoContableResponse {
    private Long id;
    private String numero;
    private LocalDate fechaContable;
    private String glosa;
    private TipoAsiento tipoAsiento;
    private EstadoAsiento estado;
    private BigDecimal totalDebe;
    private BigDecimal totalHaber;
    private List<LineaAsientoResponse> lineas;
}