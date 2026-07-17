package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Usado para reactivar o para dar de baja directa (sin turnos activos). */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CambiarEstadoEmpleadoRequest {
    private String usuario;
}
