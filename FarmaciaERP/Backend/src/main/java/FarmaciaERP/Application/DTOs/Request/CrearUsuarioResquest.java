package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrearUsuarioResquest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private RolUsuario role;
}
