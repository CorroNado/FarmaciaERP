package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SolPedResponse;
import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class RechazarSolPedUseCase {

    private final ISolicitudPedidoRepository solicitudPedidoRepository;

    public RechazarSolPedUseCase(ISolicitudPedidoRepository solicitudPedidoRepository) {
        this.solicitudPedidoRepository = solicitudPedidoRepository;
    }

    public SolPedResponse ejecutar(Long id, String motivo) {
        SolicitudPedido solPed = solicitudPedidoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("SolPed no encontrada: " + id));
        solPed.rechazar(motivo);
        SolicitudPedido actualizada = solicitudPedidoRepository.save(solPed);
        return SolPedResponseAssembler.toResponse(actualizada);
    }
}
