package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarUsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private RolUsuario role;
    private UsuarioEstados estado;
    //private LocalDateTime registro;
    //private Integer loginAttempts;
    //private LocalDateTime lockUntil;
}
