package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearOrdenCompraRequest;
import FarmaciaERP.Application.DTOs.Response.OrdenCompraResponse;
import FarmaciaERP.Domain.Entities.*;
import FarmaciaERP.Domain.Enums.EstadoSolPed;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.03 Fase 03 - Tratamiento y emisión de la Orden de Compra (ME21N).
 * RN-OC-002: el precio y el laboratorio se heredan bloqueados del
 * Info-Record del convenio aprobado en la Fase 02; no son editables.
 */
@Service
public class CrearOrdenCompraUseCase {

    private final IOrdenCompraRepository ordenCompraRepository;
    private final ISolicitudPedidoRepository solicitudPedidoRepository;

    public CrearOrdenCompraUseCase(IOrdenCompraRepository ordenCompraRepository,
                                    ISolicitudPedidoRepository solicitudPedidoRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.solicitudPedidoRepository = solicitudPedidoRepository;
    }

    @Transactional
    public OrdenCompraResponse ejecutar(CrearOrdenCompraRequest request) {
        SolicitudPedido solPed = solicitudPedidoRepository.findById(request.getSolPedId())
                .orElseThrow(() -> new BadRequestException("SolPed no encontrada: " + request.getSolPedId()));

        if (solPed.getEstado() != EstadoSolPed.FUENTE_APROBADA) {
            throw new BadRequestException(
                    "RN-OC-002: la SolPed debe tener la fuente de aprovisionamiento aprobada (Fase 02) antes de emitir la OC");
        }

        Proveedor proveedor = solPed.getProveedor();
        Convenio convenio = solPed.getConvenio();

        List<DetalleOrdenCompra> detalles = new ArrayList<>();
        for (DetalleSolPed detalleSolPed : solPed.getDetalles()) {
            Medicamento medicamento = detalleSolPed.getMedicamento();
            // RN-OC-002: precio bloqueado — se hereda del Info-Record si existe.
            double precio = convenio.precioPactadoPara(medicamento.getId())
                    .orElse(detalleSolPed.getPrecioUnitario());
            detalles.add(new DetalleOrdenCompra(medicamento, detalleSolPed.getCantidadSugerida(), precio));
        }

        String numero = generarNumeroOC();
        OrdenCompra oc = new OrdenCompra(numero, solPed, proveedor, convenio, detalles, request.getCentroDestino());

        OrdenCompra guardada = ordenCompraRepository.save(oc);
        return OrdenCompraResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroOC() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "OC-" + Year.now().getValue() + "-" + correlativo;
    }
}
