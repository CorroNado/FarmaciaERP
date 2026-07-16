package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Entities.DisputaComercial;

public class DisputaComercialResponseAssembler {

    public static DisputaComercialResponse toResponse(DisputaComercial disputa) {
        return new DisputaComercialResponse(
                disputa.getId(),
                disputa.getNumero(),
                disputa.getExcepcionFacturacion().getId(),
                disputa.getExcepcionFacturacion().getNumero(),
                disputa.getExcepcionFacturacion().getTipoDiscrepancia(),
                disputa.getExcepcionFacturacion().getConciliacionTresVias().getOrdenCompra().getId(),
                disputa.getExcepcionFacturacion().getConciliacionTresVias().getOrdenCompra().getNumero(),
                disputa.getExcepcionFacturacion().getConciliacionTresVias().getOrdenCompra().getProveedor().getRazonSocial(),
                disputa.isCotejada(),
                disputa.isCuantificada(),
                disputa.getImpactoFinanciero(),
                disputa.isValidadaDesviacion(),
                disputa.isDisputaAbierta(),
                disputa.getRondaNegociacion(),
                disputa.getMontoContraoferta(),
                disputa.getAbsorbeAceptado(),
                disputa.isResueltaWorkflow(),
                disputa.getEstado(),
                disputa.getFecha()
        );
    }
}
