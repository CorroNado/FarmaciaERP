package FarmaciaERP.application.usecases;

import FarmaciaERP.application.dto.Response.VentaResponse;
import FarmaciaERP.domain.entities.Venta;
import FarmaciaERP.domain.exceptions.BadRequestException;
import FarmaciaERP.domain.repositories.IVentaRepository;
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
