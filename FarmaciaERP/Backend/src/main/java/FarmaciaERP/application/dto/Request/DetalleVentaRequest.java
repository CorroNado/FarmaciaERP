package FarmaciaERP.application.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaRequest {
    private int medicamentoId;
    private int cantidad;
}
