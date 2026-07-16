package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearProveedorRequest;
import FarmaciaERP.Application.DTOs.Response.ProveedorResponse;
import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class CrearProveedorUseCase {

    private final IProveedorRepository proveedorRepository;

    public CrearProveedorUseCase(IProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public ProveedorResponse ejecutar(CrearProveedorRequest request) {
        if (proveedorRepository.findByRuc(request.getRuc()).isPresent()) {
            throw new BadRequestException("Ya existe un proveedor registrado con el RUC " + request.getRuc());
        }
        Proveedor proveedor = new Proveedor(
                request.getRazonSocial(),
                request.getRuc(),
                request.getContactoEmail(),
                request.getContactoTelefono()
        );
        Proveedor guardado = proveedorRepository.save(proveedor);
        return ProveedorResponseAssembler.toResponse(guardado);
    }
}
