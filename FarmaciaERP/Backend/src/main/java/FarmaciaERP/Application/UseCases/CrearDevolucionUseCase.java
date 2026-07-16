package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearDevolucionRequest;
import FarmaciaERP.Application.DTOs.Request.DetalleDevolucionRequest;
import FarmaciaERP.Application.DTOs.Response.DevolucionResponse;
import FarmaciaERP.Domain.Entities.DetalleDevolucion;
import FarmaciaERP.Domain.Entities.DetalleVenta;
import FarmaciaERP.Domain.Entities.Devolucion;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDevolucionRepository;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * SD.07.01 - Crea la solicitud de devolución sobre una venta ya pagada,
 * SD.07.02 reingresa al stock los medicamentos devueltos y SD.07.03 deja
 * registrada la acción compensatoria (nota de crédito, reembolso o cambio)
 * acordada con el cliente.
 */
@Service
public class CrearDevolucionUseCase {

    private final IDevolucionRepository devolucionRepository;
    private final IVentaRepository ventaRepository;
    private final IMedicamentoRepository medicamentoRepository;

    public CrearDevolucionUseCase(IDevolucionRepository devolucionRepository,
                                   IVentaRepository ventaRepository,
                                   IMedicamentoRepository medicamentoRepository) {
        this.devolucionRepository = devolucionRepository;
        this.ventaRepository = ventaRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public DevolucionResponse ejecutar(CrearDevolucionRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BadRequestException("La devolución debe tener al menos un detalle");
        }

        Venta venta = ventaRepository.findById(request.getVentaId())
                .orElseThrow(() -> new BadRequestException("Venta no encontrada: " + request.getVentaId()));

        // Construye los detalles de la devolución con el precio al que se vendió
        // cada medicamento (no el precio actual del catálogo).
        List<DetalleDevolucion> detalles = new ArrayList<>();
        for (DetalleDevolucionRequest detalleRequest : request.getDetalles()) {
            Medicamento medicamento = medicamentoRepository.findById(detalleRequest.getMedicamentoId())
                    .orElseThrow(() -> new BadRequestException(
                            "Medicamento no encontrado: " + detalleRequest.getMedicamentoId()));

            double precioUnitario = venta.getDetalles().stream()
                    .filter(d -> d.getMedicamento().getId() == medicamento.getId())
                    .map(DetalleVenta::getPrecioUnitario)
                    .findFirst()
                    .orElse(medicamento.getPrecio());

            detalles.add(new DetalleDevolucion(medicamento, detalleRequest.getCantidad(), precioUnitario));
        }

        // Valida que la venta esté pagada y que las cantidades no superen lo vendido
        Devolucion devolucion = new Devolucion(venta, detalles, request.getMotivo(), request.getAccion());

        // SD.07.02 - Entrega inversa: reingresa el stock devuelto y cierra la venta
        devolucion.aplicar();

        ventaRepository.save(venta);
        for (DetalleDevolucion detalle : devolucion.getDetalles()) {
            medicamentoRepository.save(detalle.getMedicamento());
        }
        Devolucion guardada = devolucionRepository.save(devolucion);

        return DevolucionResponseAssembler.toResponse(guardada);
    }
}
