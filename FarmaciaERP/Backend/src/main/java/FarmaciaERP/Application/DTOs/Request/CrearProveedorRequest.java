package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearProveedorRequest {
    private String razonSocial;
    private String ruc;
    private String contactoEmail;
    private String contactoTelefono;
}
