package FarmaciaERP.application.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
}