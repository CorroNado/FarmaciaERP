package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ConvenioResponse;
import FarmaciaERP.Domain.Enums.EstadoConvenio;
import FarmaciaERP.Domain.Repositories.IConvenioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarConvenioUseCase {

    private final IConvenioRepository convenioRepository;

    public BuscarConvenioUseCase(IConvenioRepository convenioRepository) {
        this.convenioRepository = convenioRepository;
    }

    public Optional<ConvenioResponse> porId(Long id) {
        return convenioRepository.findById(id).map(ConvenioResponseAssembler::toResponse);
    }

    public List<ConvenioResponse> todos() {
        return convenioRepository.findAll().stream().map(ConvenioResponseAssembler::toResponse).toList();
    }

    public List<ConvenioResponse> porProveedor(Long proveedorId) {
        return convenioRepository.findByProveedorId(proveedorId).stream().map(ConvenioResponseAssembler::toResponse).toList();
    }

    public List<ConvenioResponse> porEstado(EstadoConvenio estado) {
        return convenioRepository.findByEstado(estado).stream().map(ConvenioResponseAssembler::toResponse).toList();
    }
}
