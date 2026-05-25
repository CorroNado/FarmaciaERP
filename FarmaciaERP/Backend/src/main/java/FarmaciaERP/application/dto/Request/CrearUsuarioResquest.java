package FarmaciaERP.application.dto.Request;

import FarmaciaERP.domain.enums.UserRole;
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
    private UserRole rol;
}
