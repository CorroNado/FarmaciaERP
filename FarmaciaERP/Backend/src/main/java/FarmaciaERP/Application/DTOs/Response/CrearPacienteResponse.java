package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.ValueObjects.Dni;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearPacienteResponse {
    private String dni;
}
