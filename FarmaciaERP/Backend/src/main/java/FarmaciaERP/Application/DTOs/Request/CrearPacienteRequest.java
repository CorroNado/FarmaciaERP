package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TipoSeguro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrearPacienteRequest {
    private String nombre;
    private String dni;
    private TipoSeguro tipoSeguro;
}
