package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentroCostoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long sucursalId;
    private boolean activo;
}