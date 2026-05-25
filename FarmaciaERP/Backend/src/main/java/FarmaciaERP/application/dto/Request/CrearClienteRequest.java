package FarmaciaERP.application.dto.Request;

import FarmaciaERP.domain.enums.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrearClienteRequest {
    private String nombre;
    private String apellido;
    private String dni;
    private InsuranceType insuranceType;
}
