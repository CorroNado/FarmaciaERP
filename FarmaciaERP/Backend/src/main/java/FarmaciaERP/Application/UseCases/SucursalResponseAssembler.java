package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SucursalResponse;
import FarmaciaERP.Domain.Entities.Sucursal;

public class SucursalResponseAssembler {

    public static SucursalResponse toResponse(Sucursal sucursal) {
        return new SucursalResponse(
                sucursal.getId(),
                sucursal.getCodigo(),
                sucursal.getNombre(),
                sucursal.isActiva()
        );
    }
}
