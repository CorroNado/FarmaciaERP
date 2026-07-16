package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.05 Fase 05 - paso 5.1 (cont.): Ejecutar Propuesta de Pago
 * Automática (Sistema ERP) sobre el lote validado por Tesorería.
 */
@Service
public class EjecutarPropuestaAutomaticaUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public EjecutarPropuestaAutomaticaUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    @Transactional
    public PropuestaPagoAutomaticaResponse ejecutar(Long id) {
        PropuestaPagoAutomatica propuesta = propuestaPagoAutomaticaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Propuesta de pago no encontrada: " + id));

        propuesta.ejecutarPropuestaAutomatica();

        PropuestaPagoAutomatica guardada = propuestaPagoAutomaticaRepository.save(propuesta);
        return PropuestaPagoAutomaticaResponseAssembler.toResponse(guardada);
    }
}
