package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;

public class AjusteContableRegularizacionResponseAssembler {

    public static AjusteContableRegularizacionResponse toResponse(AjusteContableRegularizacion ajuste) {
        return new AjusteContableRegularizacionResponse(
                ajuste.getId(),
                ajuste.getNumero(),
                ajuste.getDisputaComercial().getId(),
                ajuste.getDisputaComercial().getNumero(),
                ajuste.getDisputaComercial().getExcepcionFacturacion().getId(),
                ajuste.getDisputaComercial().getExcepcionFacturacion().getNumero(),
                ajuste.getDisputaComercial().getExcepcionFacturacion().getConciliacionTresVias()
                        .getOrdenCompra().getProveedor().getRazonSocial(),
                ajuste.getMontoRegularizacion(),
                ajuste.getRecibeNotaCredito(),
                ajuste.isReclamoGestionado(),
                ajuste.isNotaCreditoEnviadaProveedor(),
                ajuste.isNotaCreditoRegistrada(),
                ajuste.isAsientoRegularizacion(),
                ajuste.isDesbloqueado(),
                ajuste.isRegularizada(),
                ajuste.getEstado(),
                ajuste.getFecha()
        );
    }
}
