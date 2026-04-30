package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.EmailEmb;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
}