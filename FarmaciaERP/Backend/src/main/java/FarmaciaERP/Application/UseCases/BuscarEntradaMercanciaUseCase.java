package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.EntradaMercanciaResponse;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarEntradaMercanciaUseCase {

    private final IEntradaMercanciaRepository entradaMercanciaRepository;

    public BuscarEntradaMercanciaUseCase(IEntradaMercanciaRepository entradaMercanciaRepository) {
        this.entradaMercanciaRepository = entradaMercanciaRepository;
    }

    public Optional<EntradaMercanciaResponse> porId(Long id) {
        return entradaMercanciaRepository.findById(id).map(EntradaMercanciaResponseAssembler::toResponse);
    }

    public List<EntradaMercanciaResponse> todas() {
        return entradaMercanciaRepository.findAll().stream()
                .map(EntradaMercanciaResponseAssembler::toResponse).toList();
    }

    public List<EntradaMercanciaResponse> porOrdenCompra(Long ordenCompraId) {
        return entradaMercanciaRepository.findByOrdenCompraId(ordenCompraId).stream()
                .map(EntradaMercanciaResponseAssembler::toResponse).toList();
    }
}
