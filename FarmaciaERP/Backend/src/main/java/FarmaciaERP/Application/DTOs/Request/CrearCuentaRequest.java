package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearCuentaRequest {
    private String codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;
    private NaturalezaCuenta naturaleza;
}