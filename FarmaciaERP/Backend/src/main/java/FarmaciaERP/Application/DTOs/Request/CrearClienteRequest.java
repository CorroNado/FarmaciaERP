package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TipoSeguro;
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
    private TipoSeguro tipoSeguro;
}
