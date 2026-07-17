package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Domain.Entities.Cuenta;

public class CuentaResponseAssembler {

    public static CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getCodigo(),
                cuenta.getNombre(),
                cuenta.getTipoCuenta(),
                cuenta.getNaturaleza(),
                cuenta.isActiva()
        );
    }
}