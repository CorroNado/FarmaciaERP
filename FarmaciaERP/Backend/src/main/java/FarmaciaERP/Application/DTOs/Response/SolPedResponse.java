package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoSolPed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolPedResponse {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private String responsable;
    private String centroCosto;
    private double presupuesto;
    private List<DetalleSolPedResponse> detalles;
    private double total;
    private EstadoSolPed estado;
    private Long proveedorId;
    private String razonSocialProveedor;
    private Long convenioId;
    private String numeroConvenio;
    private String motivoRechazo;
}
