package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.OrdenCompraResponse;
import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarOrdenCompraUseCase {

    private final IOrdenCompraRepository ordenCompraRepository;

    public BuscarOrdenCompraUseCase(IOrdenCompraRepository ordenCompraRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
    }

    public Optional<OrdenCompraResponse> porId(Long id) {
        return ordenCompraRepository.findById(id).map(OrdenCompraResponseAssembler::toResponse);
    }

    public List<OrdenCompraResponse> todas() {
        return ordenCompraRepository.findAll().stream().map(OrdenCompraResponseAssembler::toResponse).toList();
    }

    public List<OrdenCompraResponse> porSolPed(Long solPedId) {
        return ordenCompraRepository.findBySolicitudPedidoId(solPedId).stream().map(OrdenCompraResponseAssembler::toResponse).toList();
    }

    public List<OrdenCompraResponse> porEstado(EstadoOrdenCompra estado) {
        return ordenCompraRepository.findByEstado(estado).stream().map(OrdenCompraResponseAssembler::toResponse).toList();
    }
}
