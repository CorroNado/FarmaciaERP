package FarmaciaERP.Application.DTOs.Response;

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
public class CuentaResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;
    private NaturalezaCuenta naturaleza;
    private boolean activa;
}