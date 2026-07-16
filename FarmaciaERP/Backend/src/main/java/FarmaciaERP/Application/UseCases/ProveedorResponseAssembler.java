package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ProveedorResponse;
import FarmaciaERP.Domain.Entities.Proveedor;

public class ProveedorResponseAssembler {

    public static ProveedorResponse toResponse(Proveedor proveedor) {
        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getRazonSocial(),
                proveedor.getRuc(),
                proveedor.getContactoEmail(),
                proveedor.getContactoTelefono(),
                proveedor.getEstado()
        );
    }
}
