package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoDisputaComercial;
import FarmaciaERP.Domain.Enums.TipoDiscrepancia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisputaComercialResponse {
    private Long id;
    private String numero;
    private Long excepcionFacturacionId;
    private String numeroExcepcion;
    private TipoDiscrepancia tipoDiscrepancia;
    private Long ordenCompraId;
    private String numeroOrdenCompra;
    private String razonSocialProveedor;
    private boolean cotejada;
    private boolean cuantificada;
    private double impactoFinanciero;
    private boolean validadaDesviacion;
    private boolean disputaAbierta;
    private int rondaNegociacion;
    private double montoContraoferta;
    private Boolean absorbeAceptado;
    private boolean resueltaWorkflow;
    private EstadoDisputaComercial estado;
    private LocalDateTime fecha;
}
