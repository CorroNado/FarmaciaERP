package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarProveedorUseCase {

    private final IProveedorRepository proveedorRepository;

    public EliminarProveedorUseCase(IProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public void ejecutar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new BadRequestException("Proveedor no encontrado: " + id);
        }
        proveedorRepository.deleteById(id);
    }
}
