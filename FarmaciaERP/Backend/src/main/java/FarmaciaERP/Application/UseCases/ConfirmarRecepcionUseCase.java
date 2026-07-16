package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.OrdenTrasladoResponse;
import FarmaciaERP.Domain.Entities.OrdenTraslado;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IOrdenTrasladoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LOG.07 Fase 06 - RN-E6-009: la sucursal confirma en el POS la recepción
 * física de la STO. El stock deja de estar "en tránsito" y queda
 * disponible para la venta en el punto de destino.
 */
@Service
public class ConfirmarRecepcionUseCase {

    private final IOrdenTrasladoRepository ordenTrasladoRepository;

    public ConfirmarRecepcionUseCase(IOrdenTrasladoRepository ordenTrasladoRepository) {
        this.ordenTrasladoRepository = ordenTrasladoRepository;
    }

    @Transactional
    public OrdenTrasladoResponse ejecutar(Long ordenTrasladoId) {
        OrdenTraslado orden = ordenTrasladoRepository.findById(ordenTrasladoId)
                .orElseThrow(() -> new BadRequestException("Orden de Traslado (STO) no encontrada: " + ordenTrasladoId));

        orden.confirmarRecepcion();

        OrdenTraslado actualizada = ordenTrasladoRepository.save(orden);
        return OrdenTrasladoResponseAssembler.toResponse(actualizada);
    }
}
