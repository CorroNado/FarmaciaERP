package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoProveedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorResponse {
    private Long id;
    private String razonSocial;
    private String ruc;
    private String contactoEmail;
    private String contactoTelefono;
    private EstadoProveedor estado;
}
