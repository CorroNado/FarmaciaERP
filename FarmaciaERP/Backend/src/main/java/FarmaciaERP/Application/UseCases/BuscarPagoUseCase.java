package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PagoResponse;
import FarmaciaERP.Domain.Repositories.IPagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarPagoUseCase {

    private final IPagoRepository pagoRepository;

    public BuscarPagoUseCase(IPagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public Optional<PagoResponse> porId(Long id) {
        return pagoRepository.findById(id).map(PagoResponseAssembler::toResponse);
    }

    public List<PagoResponse> todos() {
        return pagoRepository.findAll().stream()
                .map(PagoResponseAssembler::toResponse).toList();
    }

    public List<PagoResponse> porFacturaMIRO(Long facturaMIROId) {
        return pagoRepository.findByFacturaMIROId(facturaMIROId).stream()
                .map(PagoResponseAssembler::toResponse).toList();
    }
}
