package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearVentaRequest;
import FarmaciaERP.Application.DTOs.Request.DetalleVentaRequest;
import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Entities.DetalleVenta;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrearVentaUseCase {

    private final IVentaRepository ventaRepository;
    private final IClienteRepository clienteRepository;
    private final IMedicamentoRepository medicamentoRepository;
    private final AsientoContableService asientoContableService;

    public CrearVentaUseCase(IVentaRepository ventaRepository,
                              IClienteRepository clienteRepository,
                              IMedicamentoRepository medicamentoRepository,
                              AsientoContableService asientoContableService) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.asientoContableService = asientoContableService;
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

        // Generar asiento contable de venta
        asientoContableService.generarAsientoVenta(guardada);

        return VentaResponseAssembler.toResponse(guardada);
    }
}
