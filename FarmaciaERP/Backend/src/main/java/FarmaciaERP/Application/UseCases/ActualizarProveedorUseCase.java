package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ActualizarProveedorRequest;
import FarmaciaERP.Application.DTOs.Response.ProveedorResponse;
import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarProveedorUseCase {

    private final IProveedorRepository proveedorRepository;

    public ActualizarProveedorUseCase(IProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public ProveedorResponse ejecutar(Long id, ActualizarProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Proveedor no encontrado: " + id));

        if (request.getRazonSocial() != null && !request.getRazonSocial().isBlank()) {
            proveedor.setRazonSocial(request.getRazonSocial());
        }
        if (request.getContactoEmail() != null) {
            proveedor.setContactoEmail(request.getContactoEmail());
        }
        if (request.getContactoTelefono() != null) {
            proveedor.setContactoTelefono(request.getContactoTelefono());
        }
        Proveedor actualizado = proveedorRepository.save(proveedor);
        return ProveedorResponseAssembler.toResponse(actualizado);
    }
}
