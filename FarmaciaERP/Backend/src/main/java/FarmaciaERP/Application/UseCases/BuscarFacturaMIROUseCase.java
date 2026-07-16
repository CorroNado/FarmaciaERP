package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.FacturaMIROResponse;
import FarmaciaERP.Domain.Repositories.IFacturaMIRORepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarFacturaMIROUseCase {

    private final IFacturaMIRORepository facturaMIRORepository;

    public BuscarFacturaMIROUseCase(IFacturaMIRORepository facturaMIRORepository) {
        this.facturaMIRORepository = facturaMIRORepository;
    }

    public Optional<FacturaMIROResponse> porId(Long id) {
        return facturaMIRORepository.findById(id).map(FacturaMIROResponseAssembler::toResponse);
    }

    public List<FacturaMIROResponse> todas() {
        return facturaMIRORepository.findAll().stream()
                .map(FacturaMIROResponseAssembler::toResponse).toList();
    }

    public List<FacturaMIROResponse> porOrdenCompra(Long ordenCompraId) {
        return facturaMIRORepository.findByOrdenCompraId(ordenCompraId).stream()
                .map(FacturaMIROResponseAssembler::toResponse).toList();
    }
}
