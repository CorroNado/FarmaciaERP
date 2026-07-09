package FarmaciaERP.application.usecases;

import FarmaciaERP.application.dto.Response.VentaResponse;
import FarmaciaERP.domain.entities.DetalleVenta;
import FarmaciaERP.domain.entities.Venta;
import FarmaciaERP.domain.exceptions.BadRequestException;
import FarmaciaERP.domain.repositories.IMedicamentoRepository;
import FarmaciaERP.domain.repositories.IVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnularVentaUseCase {

    private final IVentaRepository ventaRepository;
    private final IMedicamentoRepository medicamentoRepository;

    public AnularVentaUseCase(IVentaRepository ventaRepository, IMedicamentoRepository medicamentoRepository) {
        this.ventaRepository = ventaRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public VentaResponse ejecutar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Venta no encontrada: " + id));

        venta.anular();
        Venta actualizada = ventaRepository.save(venta);

        // Repone el stock real de cada medicamento afectado (SD.07.02 Entrega inversa)
        for (DetalleVenta detalle : venta.getDetalles()) {
            medicamentoRepository.save(detalle.getMedicamento());
        }

        return VentaResponseAssembler.toResponse(actualizada);
    }
}
