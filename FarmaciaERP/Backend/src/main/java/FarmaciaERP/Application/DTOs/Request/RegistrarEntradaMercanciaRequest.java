package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarEntradaMercanciaRequest {
    private Long ordenCompraId;
    private String lote;
    private String fechaVencimiento;
    private double temperaturaArribo;
    private int cantidadRecibida;
    // RN-F04-003: permite continuar como excepción documentada cuando la
    // diferencia frente a la OC supera el 2% de tolerancia.
    private boolean confirmarExcepcion;
}
