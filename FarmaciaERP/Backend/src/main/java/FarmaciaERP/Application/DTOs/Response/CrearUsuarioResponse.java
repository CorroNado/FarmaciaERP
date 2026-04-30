package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.ValueObjects.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CrearUsuarioResponse {
    private String email;
}
