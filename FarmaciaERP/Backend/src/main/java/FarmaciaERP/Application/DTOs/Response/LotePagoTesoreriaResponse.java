package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoLotePago;
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
public class LotePagoTesoreriaResponse {
    private Long id;
    private String numero;
    private List<DetalleLotePagoResponse> detalles;
    private double montoNetoRegularizado;
    private boolean proveedoresCriticosPriorizados;
    private boolean descuentoProntoPagoNegociado;
    private double descuentoProntoPagoPct;
    private boolean lotePreparado;
    private double montoLote;
    private boolean fondosVerificados;
    private int revisionesComite;
    private boolean loteCorregido;
    private boolean aprobadoPorComite;
    private boolean pagosConciliadosGestion;
    private EstadoLotePago estado;
    private LocalDateTime fecha;
}
