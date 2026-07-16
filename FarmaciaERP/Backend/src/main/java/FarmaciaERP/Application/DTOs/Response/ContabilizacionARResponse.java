package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoContabilizacionAR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContabilizacionARResponse {
    private Long id;
    private Long cierreCajaId;
    private String cierreCajaNumero;
    private LocalDateTime fecha;
    private boolean tieneVariacion;
    private boolean conciliacionPOS;
    private boolean asientoProcesado;
    private boolean ajusteDescuadre;
    private boolean recetasAuditadas;
    private Boolean recetasCorrectas;
    private String motivoObservacion;
    private boolean subsanacion;
    private boolean consolidado;
    private EstadoContabilizacionAR estado;
    private boolean puedeContinuarFase03;
}
