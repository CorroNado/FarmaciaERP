package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CotizacionResponse;
import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICotizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SD.02.04 - El cliente rechaza la oferta económica; se registra el motivo
 * para trazabilidad comercial.
 */
@Service
public class RechazarCotizacionUseCase {

    private final ICotizacionRepository cotizacionRepository;

    public RechazarCotizacionUseCase(ICotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    @Transactional
    public CotizacionResponse ejecutar(Long id, String motivo) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cotización no encontrada: " + id));

        cotizacion.rechazar(motivo);
        Cotizacion actualizada = cotizacionRepository.save(cotizacion);

        return CotizacionResponseAssembler.toResponse(actualizada);
    }
}
