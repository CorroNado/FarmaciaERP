package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Entities.DetalleVenta;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnularVentaUseCase {

    private final IVentaRepository ventaRepository;
    private final IMedicamentoRepository medicamentoRepository;
    private final IAsientoContableRepository asientoContableRepository;

    public AnularVentaUseCase(IVentaRepository ventaRepository, IMedicamentoRepository medicamentoRepository,
                               IAsientoContableRepository asientoContableRepository) {
        this.ventaRepository = ventaRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.asientoContableRepository = asientoContableRepository;
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

        // Anula los asientos contables generados por esta venta, si existen
        anularAsientoSiExiste("VTA-" + id);
        anularAsientoSiExiste("VTA-COBRO-" + id);

        return VentaResponseAssembler.toResponse(actualizada);
    }

    private void anularAsientoSiExiste(String numero) {
        asientoContableRepository.findByNumero(numero).ifPresent(asiento -> {
            if (asiento.getEstado() != EstadoAsiento.ANULADO) {
                asiento.anular();
                asientoContableRepository.save(asiento);
            }
        });
    }
}
