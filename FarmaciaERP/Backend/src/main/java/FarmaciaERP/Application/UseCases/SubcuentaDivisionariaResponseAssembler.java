package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;

public class SubcuentaDivisionariaResponseAssembler {

    public static SubcuentaDivisionariaResponse toResponse(SubcuentaDivisionaria subcuenta) {
        return new SubcuentaDivisionariaResponse(
                subcuenta.getId(),
                subcuenta.getCodigo(),
                subcuenta.getNombre(),
                subcuenta.getCuenta().getId(),
                subcuenta.getCuenta().getCodigo(),
                subcuenta.isActiva()
        );
    }
}