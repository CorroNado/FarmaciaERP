package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearVentaRequest;
import FarmaciaERP.Application.DTOs.Request.DetalleVentaRequest;
import FarmaciaERP.Application.DTOs.Response.AceptarCotizacionResponse;
import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Entities.DetalleCotizacion;
import FarmaciaERP.Domain.Enums.TipoComprobante;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICotizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * SD.02.03 -> SD.03.01 - El cliente acepta la cotización y esta se convierte
 * en un pedido de venta (Venta en estado PENDIENTE), reutilizando
 * CrearVentaUseCase para no duplicar las reglas de creación de venta.
 */
@Service
public class AceptarCotizacionUseCase {

    private final ICotizacionRepository cotizacionRepository;
    private final CrearVentaUseCase crearVentaUseCase;

    public AceptarCotizacionUseCase(ICotizacionRepository cotizacionRepository,
                                     CrearVentaUseCase crearVentaUseCase) {
        this.cotizacionRepository = cotizacionRepository;
        this.crearVentaUseCase = crearVentaUseCase;
    }

    @Transactional
    public AceptarCotizacionResponse ejecutar(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cotización no encontrada: " + id));

        List<DetalleVentaRequest> detallesVenta = new ArrayList<>();
        for (DetalleCotizacion detalle : cotizacion.getDetalles()) {
            detallesVenta.add(new DetalleVentaRequest(detalle.getMedicamento().getId(), detalle.getCantidad()));
        }
        CrearVentaRequest ventaRequest = new CrearVentaRequest(
                cotizacion.getCliente().getId(), null, TipoComprobante.NINGUNO, detallesVenta);

        // SD.03.02/SD.03.03 - Verifica stock y descuenta al confirmar el pedido de venta
        VentaResponse venta = crearVentaUseCase.ejecutar(ventaRequest);

        cotizacion.aceptar(venta.getId());
        Cotizacion actualizada = cotizacionRepository.save(cotizacion);

        return new AceptarCotizacionResponse(CotizacionResponseAssembler.toResponse(actualizada), venta);
    }
}
