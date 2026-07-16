package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Domain.Entities.CentroCosto;

public class CentroCostoResponseAssembler {

    public static CentroCostoResponse toResponse(CentroCosto centroCosto) {
        return new CentroCostoResponse(
                centroCosto.getId(),
                centroCosto.getCodigo(),
                centroCosto.getNombre(),
                centroCosto.getSucursal() == null ? null : centroCosto.getSucursal().getId(),
                centroCosto.isActivo()
        );
    }
}