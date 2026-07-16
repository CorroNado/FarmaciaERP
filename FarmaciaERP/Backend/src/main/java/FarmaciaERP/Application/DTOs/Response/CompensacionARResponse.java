package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoCompensacionAR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompensacionARResponse {
    private Long id;
    private Long contabilizacionARId;
    private boolean compensado;
    private boolean reporteGenerado;
    private double montoVentas;
    private double montoAprobadas;
    private double perdidas;
    private double margenNeto;
    private double margenPct;
    private boolean saldoConfirmado;
    private boolean cerrado;
    private EstadoCompensacionAR estado;
    private LocalDateTime fecha;
    private LocalDateTime fechaCierre;
    private boolean finalizado;
}
