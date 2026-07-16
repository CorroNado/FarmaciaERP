package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DetalleLotePagoResponse;
import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;

public class LotePagoTesoreriaResponseAssembler {

    public static LotePagoTesoreriaResponse toResponse(LotePagoTesoreria lote) {
        return new LotePagoTesoreriaResponse(
                lote.getId(),
                lote.getNumero(),
                lote.getAjustesContables().stream().map(LotePagoTesoreriaResponseAssembler::toDetalle).toList(),
                lote.getMontoNetoRegularizado(),
                lote.isProveedoresCriticosPriorizados(),
                lote.isDescuentoProntoPagoNegociado(),
                lote.getDescuentoProntoPagoPct(),
                lote.isLotePreparado(),
                lote.getMontoLote(),
                lote.isFondosVerificados(),
                lote.getRevisionesComite(),
                lote.isLoteCorregido(),
                lote.isAprobadoPorComite(),
                lote.isPagosConciliadosGestion(),
                lote.getEstado(),
                lote.getFecha()
        );
    }

    private static DetalleLotePagoResponse toDetalle(AjusteContableRegularizacion ajuste) {
        return new DetalleLotePagoResponse(
                ajuste.getId(),
                ajuste.getNumero(),
                ajuste.getDisputaComercial().getExcepcionFacturacion().getNumero(),
                ajuste.getDisputaComercial().getExcepcionFacturacion().getConciliacionTresVias()
                        .getOrdenCompra().getProveedor().getRazonSocial(),
                ajuste.getMontoNetoPagar()
        );
    }
}
