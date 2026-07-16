package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarFacturaMIRORequest {
    private Long ordenCompraId;
    private String numeroFactura;
    private String fechaEmision;
}
