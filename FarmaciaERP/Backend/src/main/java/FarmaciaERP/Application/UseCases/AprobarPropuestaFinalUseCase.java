package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.05 Fase 05 - paso 5.2 (cont.): Aprobar Propuesta de Pago Final
 * (Analista de Cuentas por Pagar), sin excepciones ni bloqueos
 * pendientes.
 */
@Service
public class AprobarPropuestaFinalUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public AprobarPropuestaFinalUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    @Transactional
    public PropuestaPagoAutomaticaResponse ejecutar(Long id) {
        PropuestaPagoAutomatica propuesta = propuestaPagoAutomaticaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Propuesta de pago no encontrada: " + id));

        propuesta.aprobarPropuestaFinal();

        PropuestaPagoAutomatica guardada = propuestaPagoAutomaticaRepository.save(propuesta);
        return PropuestaPagoAutomaticaResponseAssembler.toResponse(guardada);
    }
}
