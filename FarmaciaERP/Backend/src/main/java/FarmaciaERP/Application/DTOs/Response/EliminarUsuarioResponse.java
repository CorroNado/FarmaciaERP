package FarmaciaERP.Application.DTOs.Response;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EliminarUsuarioResponse {
    private Long id;
    private String mensaje;
}
