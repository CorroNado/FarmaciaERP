package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TipoAsiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearAsientoContableRequest {
    private String numero;
    private LocalDate fechaContable;
    private String glosa;
    private TipoAsiento tipoAsiento;
    private List<CrearLineaAsientoRequest> lineas;
}