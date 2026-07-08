package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrarPagoVentaUseCase {

    private final IVentaRepository ventaRepository;

    public RegistrarPagoVentaUseCase(IVentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public VentaResponse ejecutar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Venta no encontrada: " + id));

        venta.registrarPago();
        Venta actualizada = ventaRepository.save(venta);
        return VentaResponseAssembler.toResponse(actualizada);
    }
}
