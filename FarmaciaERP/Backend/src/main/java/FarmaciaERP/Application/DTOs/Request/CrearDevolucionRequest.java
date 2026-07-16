package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.AccionDevolucion;
import FarmaciaERP.Domain.Enums.MotivoDevolucion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearDevolucionRequest {
    private Long ventaId;
    private MotivoDevolucion motivo;
    private AccionDevolucion accion;
    private List<DetalleDevolucionRequest> detalles;
}
