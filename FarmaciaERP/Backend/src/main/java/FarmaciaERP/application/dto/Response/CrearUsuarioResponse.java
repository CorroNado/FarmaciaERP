package FarmaciaERP.application.dto.Response;

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
    private String mensaje;
}
