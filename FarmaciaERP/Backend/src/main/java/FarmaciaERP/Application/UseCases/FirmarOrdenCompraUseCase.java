package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.OrdenCompraResponse;
import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LOG.03 Fase 03 - RN-OC-003 (fecha límite obligatoria), RN-OC-006 y
 * RN-OC-008 (firma digital y despacho electrónico al proveedor).
 */
@Service
public class FirmarOrdenCompraUseCase {

    private final IOrdenCompraRepository ordenCompraRepository;
    private final ISolicitudPedidoRepository solicitudPedidoRepository;

    public FirmarOrdenCompraUseCase(IOrdenCompraRepository ordenCompraRepository,
                                     ISolicitudPedidoRepository solicitudPedidoRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.solicitudPedidoRepository = solicitudPedidoRepository;
    }

    @Transactional
    public OrdenCompraResponse ejecutar(Long ocId, String fechaEntregaLimite) {
        OrdenCompra oc = ordenCompraRepository.findById(ocId)
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada: " + ocId));

        oc.firmar(fechaEntregaLimite);
        OrdenCompra firmada = ordenCompraRepository.save(oc);

        SolicitudPedido solPed = solicitudPedidoRepository.findById(oc.getSolicitudPedido().getId())
                .orElseThrow(() -> new BadRequestException("SolPed no encontrada"));
        solPed.convertirEnOrdenCompra();
        solicitudPedidoRepository.save(solPed);

        return OrdenCompraResponseAssembler.toResponse(firmada);
    }
}
