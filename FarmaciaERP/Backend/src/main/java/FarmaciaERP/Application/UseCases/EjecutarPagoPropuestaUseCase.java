package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.05 Fase 05 - paso 5.3: Ejecutar Ejecución de Pago (Sistema ERP)
 * sobre la propuesta de pago final ya aprobada.
 */
@Service
public class EjecutarPagoPropuestaUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public EjecutarPagoPropuestaUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    @Transactional
    public PropuestaPagoAutomaticaResponse ejecutar(Long id) {
        PropuestaPagoAutomatica propuesta = propuestaPagoAutomaticaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Propuesta de pago no encontrada: " + id));

        propuesta.ejecutarPago();

        PropuestaPagoAutomatica guardada = propuestaPagoAutomaticaRepository.save(propuesta);
        return PropuestaPagoAutomaticaResponseAssembler.toResponse(guardada);
    }
}
