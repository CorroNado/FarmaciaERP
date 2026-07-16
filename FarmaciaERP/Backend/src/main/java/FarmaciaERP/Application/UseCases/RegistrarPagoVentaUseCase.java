package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrarPagoVentaUseCase {

    private final IVentaRepository ventaRepository;
    private final AsientoContableService asientoContableService;

    public RegistrarPagoVentaUseCase(IVentaRepository ventaRepository, AsientoContableService asientoContableService) {
        this.ventaRepository = ventaRepository;
        this.asientoContableService = asientoContableService;
    }

    public VentaResponse ejecutar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Venta no encontrada: " + id));

        venta.registrarPago();
        Venta actualizada = ventaRepository.save(venta);

        // Generar asiento contable por el cobro/pago de la venta
        asientoContableService.generarAsientoPagoVenta(actualizada);

        return VentaResponseAssembler.toResponse(actualizada);
    }
}
