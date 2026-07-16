package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearSolPedRequest;
import FarmaciaERP.Application.DTOs.Request.DetalleSolPedRequest;
import FarmaciaERP.Application.DTOs.Response.SolPedResponse;
import FarmaciaERP.Domain.Entities.DetalleSolPed;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.01 Fase 01 - Planificación de necesidades (MRP). RN-E1-004: toda
 * necesidad debe estar respaldada por el cálculo MRP. RN-E1-006: las
 * cantidades deben ser mayores a cero. RN-E1-010: la SolPed se libera
 * automáticamente hacia la Fase 02 al crearse.
 */
@Service
public class CrearSolPedUseCase {

    private final ISolicitudPedidoRepository solicitudPedidoRepository;
    private final IMedicamentoRepository medicamentoRepository;

    public CrearSolPedUseCase(ISolicitudPedidoRepository solicitudPedidoRepository,
                               IMedicamentoRepository medicamentoRepository) {
        this.solicitudPedidoRepository = solicitudPedidoRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public SolPedResponse ejecutar(CrearSolPedRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BadRequestException("RN-E1-004: la SolPed debe tener al menos un ítem respaldado por MRP");
        }

        List<DetalleSolPed> detalles = new ArrayList<>();
        for (DetalleSolPedRequest detalleRequest : request.getDetalles()) {
            Medicamento medicamento = medicamentoRepository.findById(detalleRequest.getMedicamentoId())
                    .orElseThrow(() -> new BadRequestException(
                            "Medicamento no encontrado: " + detalleRequest.getMedicamentoId()));

            detalles.add(new DetalleSolPed(
                    medicamento,
                    medicamento.getStock(),
                    detalleRequest.getStockMinimo(),
                    detalleRequest.getCantidadSugerida(),
                    medicamento.getPrecio()
            ));
        }

        String numero = generarNumeroSolPed();
        SolicitudPedido solPed = new SolicitudPedido(numero, request.getResponsable(), request.getCentroCosto(),
                request.getPresupuesto(), detalles);

        SolicitudPedido guardada = solicitudPedidoRepository.save(solPed);
        return SolPedResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroSolPed() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "SOLPED-" + Year.now().getValue() + "-" + correlativo;
    }
}
