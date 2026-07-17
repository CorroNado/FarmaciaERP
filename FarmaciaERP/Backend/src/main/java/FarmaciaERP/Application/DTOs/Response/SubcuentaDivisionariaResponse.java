package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcuentaDivisionariaResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long cuentaId;
    private String cuentaCodigo;
    private boolean activa;
}