package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearSolPedRequest {
    private String responsable;
    private String centroCosto;
    private double presupuesto;
    private List<DetalleSolPedRequest> detalles;
}
