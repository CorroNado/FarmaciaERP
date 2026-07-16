package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.IntroducirParametrosPagoRequest;
import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.05 Fase 05 - paso 5.1: Introducir Parámetros de Pago en F110
 * (fecha, sociedad, vía de pago, lote de proveedores) — Analista de
 * Cuentas por Pagar.
 */
@Service
public class IntroducirParametrosPagoUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public IntroducirParametrosPagoUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    @Transactional
    public PropuestaPagoAutomaticaResponse ejecutar(Long id, IntroducirParametrosPagoRequest request) {
        PropuestaPagoAutomatica propuesta = propuestaPagoAutomaticaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Propuesta de pago no encontrada: " + id));

        propuesta.introducirParametrosPago(request.getSociedad(), request.getViaPago(), request.getFechaPago());

        PropuestaPagoAutomatica guardada = propuestaPagoAutomaticaRepository.save(propuesta);
        return PropuestaPagoAutomaticaResponseAssembler.toResponse(guardada);
    }
}
