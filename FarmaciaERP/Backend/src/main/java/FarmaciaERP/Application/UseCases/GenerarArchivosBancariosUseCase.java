package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.05 Fase 05 - paso 5.3 (cont.): Generar Archivos Bancarios Planos
 * (IDoc / N43) para envío multi-bancario. Concluye la Fase 05 y habilita
 * la Fase 06 (Dispersión Bancaria y Conciliación de Cierre).
 */
@Service
public class GenerarArchivosBancariosUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public GenerarArchivosBancariosUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    @Transactional
    public PropuestaPagoAutomaticaResponse ejecutar(Long id) {
        PropuestaPagoAutomatica propuesta = propuestaPagoAutomaticaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Propuesta de pago no encontrada: " + id));

        propuesta.generarArchivosBancarios();

        PropuestaPagoAutomatica guardada = propuestaPagoAutomaticaRepository.save(propuesta);
        return PropuestaPagoAutomaticaResponseAssembler.toResponse(guardada);
    }
}
