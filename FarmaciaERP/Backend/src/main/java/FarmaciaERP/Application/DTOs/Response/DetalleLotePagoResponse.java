package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleLotePagoResponse {
    private Long ajusteContableId;
    private String numeroAjusteContable;
    private String numeroExcepcion;
    private String razonSocialProveedor;
    private double montoNetoPagar;
}
