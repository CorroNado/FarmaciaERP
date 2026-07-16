package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.IniciarPropuestaPagoRequest;
import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FI-AP.05 Fase 05 - Inicio de la propuesta de pago automática a partir
 * del lote de pagos ya conciliado a nivel de gestión en la Fase 04.
 */
@Service
public class IniciarPropuestaPagoUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;
    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public IniciarPropuestaPagoUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository,
                                        ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public PropuestaPagoAutomaticaResponse ejecutar(IniciarPropuestaPagoRequest request) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(request.getLotePagoTesoreriaId())
                .orElseThrow(() -> new BadRequestException(
                        "Lote de pagos no encontrado: " + request.getLotePagoTesoreriaId()));

        // RN-AP5-01: un lote de pagos ya usado en una propuesta no puede reutilizarse en otra.
        if (propuestaPagoAutomaticaRepository.existsByLotePagoTesoreriaId(lote.getId())) {
            throw new BadRequestException(
                    "El lote de pagos " + lote.getNumero() + " ya cuenta con una propuesta de pago automática");
        }

        String numero = generarNumeroPropuesta();
        PropuestaPagoAutomatica propuesta = PropuestaPagoAutomatica.iniciar(numero, lote);

        PropuestaPagoAutomatica guardada = propuestaPagoAutomaticaRepository.save(propuesta);
        return PropuestaPagoAutomaticaResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroPropuesta() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "PPA-" + Year.now().getValue() + "-" + correlativo;
    }
}
