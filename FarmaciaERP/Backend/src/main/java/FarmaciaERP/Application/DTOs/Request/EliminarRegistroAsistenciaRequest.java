package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EliminarRegistroAsistenciaRequest {
    private String usuario;
    private String motivoAuditoria;
}
