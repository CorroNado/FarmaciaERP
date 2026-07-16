package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SolPedResponse;
import FarmaciaERP.Domain.Enums.EstadoSolPed;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarSolPedUseCase {

    private final ISolicitudPedidoRepository solicitudPedidoRepository;

    public BuscarSolPedUseCase(ISolicitudPedidoRepository solicitudPedidoRepository) {
        this.solicitudPedidoRepository = solicitudPedidoRepository;
    }

    public Optional<SolPedResponse> porId(Long id) {
        return solicitudPedidoRepository.findById(id).map(SolPedResponseAssembler::toResponse);
    }

    public List<SolPedResponse> todas() {
        return solicitudPedidoRepository.findAll().stream().map(SolPedResponseAssembler::toResponse).toList();
    }

    public List<SolPedResponse> porEstado(EstadoSolPed estado) {
        return solicitudPedidoRepository.findByEstado(estado).stream().map(SolPedResponseAssembler::toResponse).toList();
    }
}
