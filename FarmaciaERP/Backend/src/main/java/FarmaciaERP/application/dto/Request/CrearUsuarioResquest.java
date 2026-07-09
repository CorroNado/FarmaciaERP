package FarmaciaERP.application.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearUsuarioResquest {
    private Long perfilId;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
}
