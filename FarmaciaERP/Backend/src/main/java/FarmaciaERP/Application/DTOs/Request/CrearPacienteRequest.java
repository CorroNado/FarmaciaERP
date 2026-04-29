package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrearPacienteRequest {
    private FullName nombre;
    private Dni dni;
    private TipoSeguro tipoSeguro;
}
