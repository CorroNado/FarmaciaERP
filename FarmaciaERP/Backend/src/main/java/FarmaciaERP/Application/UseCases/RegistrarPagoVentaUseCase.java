package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarPagoVentaUseCase {
    private final IVentaRepository ventaRepository;
    private final GenerarAsientoVentaUseCase generarAsientoVentaUseCase;

    public VentaResponse ejecutar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Venta no encontrada: " + id));

        venta.registrarPago();
        Venta actualizada = ventaRepository.save(venta);

        // Generar asiento contable del cobro (Debe Caja / Haber Clientes)
        generarAsientoVentaUseCase.generarAsientoCobro(actualizada);

        return VentaResponseAssembler.toResponse(actualizada);
    }
}
