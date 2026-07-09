package FarmaciaERP.application.usecases;

import FarmaciaERP.application.dto.Request.CrearVentaRequest;
import FarmaciaERP.application.dto.Request.DetalleVentaRequest;
import FarmaciaERP.application.dto.Response.VentaResponse;
import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.entities.DetalleVenta;
import FarmaciaERP.domain.entities.Medicamento;
import FarmaciaERP.domain.entities.Venta;
import FarmaciaERP.domain.exceptions.BadRequestException;
import FarmaciaERP.domain.repositories.IClienteRepository;
import FarmaciaERP.domain.repositories.IMedicamentoRepository;
import FarmaciaERP.domain.repositories.IVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrearVentaUseCase {

    private final IVentaRepository ventaRepository;
    private final IClienteRepository clienteRepository;
    private final IMedicamentoRepository medicamentoRepository;

    public CrearVentaUseCase(IVentaRepository ventaRepository,
                              IClienteRepository clienteRepository,
                              IMedicamentoRepository medicamentoRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public VentaResponse ejecutar(CrearVentaRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BadRequestException("La venta debe tener al menos un detalle");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new BadRequestException("Cliente no encontrado: " + request.getClienteId()));

        // SD.03.02 - Construir detalles y validar precios/medicamentos existentes
        List<DetalleVenta> detalles = new ArrayList<>();
        for (DetalleVentaRequest detalleRequest : request.getDetalles()) {
            Medicamento medicamento = medicamentoRepository.findById(detalleRequest.getMedicamentoId())
                    .orElseThrow(() -> new BadRequestException(
                            "Medicamento no encontrado: " + detalleRequest.getMedicamentoId()));

            detalles.add(new DetalleVenta(medicamento, detalleRequest.getCantidad(), medicamento.getPrecio()));
        }

        Venta venta = new Venta(cliente, detalles, request.getMetodoPago(), request.getTipoComprobante());

        // SD.03.03 - Verificacion dinamica de disponibilidad (ATP check) + descuento de stock
        venta.confirmarStock();

        Venta guardada = ventaRepository.save(venta);

        // SD.05 - Persistir el nuevo stock de cada medicamento afectado
        for (DetalleVenta detalle : detalles) {
            medicamentoRepository.save(detalle.getMedicamento());
        }

        return VentaResponseAssembler.toResponse(guardada);
    }
}
