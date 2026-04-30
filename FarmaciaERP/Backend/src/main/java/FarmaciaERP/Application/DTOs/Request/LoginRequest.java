package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.RolUsuario;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}