package FarmaciaERP.Application.DTOs.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor; 

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarUsuarioResponse {
    private Long id;
    private String mensaje;
}
