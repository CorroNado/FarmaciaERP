package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerarOrdenTrasladoRequest {
    private Long inspeccionCalidadId;
    private Long sucursalDestinoId;
    private String guiaRemision;
}
