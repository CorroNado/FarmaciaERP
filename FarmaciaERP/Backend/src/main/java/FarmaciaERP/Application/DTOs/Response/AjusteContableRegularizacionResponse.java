package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoAjusteContable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AjusteContableRegularizacionResponse {
    private Long id;
    private String numero;
    private Long disputaComercialId;
    private String numeroDisputaComercial;
    private Long excepcionFacturacionId;
    private String numeroExcepcion;
    private String razonSocialProveedor;
    private double montoRegularizacion;
    private Boolean recibeNotaCredito;
    private boolean reclamoGestionado;
    private boolean notaCreditoEnviadaProveedor;
    private boolean notaCreditoRegistrada;
    private boolean asientoRegularizacion;
    private boolean desbloqueado;
    private boolean regularizada;
    private EstadoAjusteContable estado;
    private LocalDateTime fecha;
}
