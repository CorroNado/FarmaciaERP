package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ProveedorResponse;
import FarmaciaERP.Domain.Enums.EstadoProveedor;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarProveedorUseCase {

    private final IProveedorRepository proveedorRepository;

    public BuscarProveedorUseCase(IProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public Optional<ProveedorResponse> porId(Long id) {
        return proveedorRepository.findById(id).map(ProveedorResponseAssembler::toResponse);
    }

    public List<ProveedorResponse> todos() {
        return proveedorRepository.findAll().stream().map(ProveedorResponseAssembler::toResponse).toList();
    }

    public List<ProveedorResponse> porRazonSocial(String razonSocial) {
        return proveedorRepository.findByRazonSocial(razonSocial).stream().map(ProveedorResponseAssembler::toResponse).toList();
    }

    public List<ProveedorResponse> porEstado(EstadoProveedor estado) {
        return proveedorRepository.findByEstado(estado).stream().map(ProveedorResponseAssembler::toResponse).toList();
    }
}
