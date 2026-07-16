package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.IniciarDispersionBancariaRequest;
import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;
import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDispersionBancariaCierreRepository;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FI-AP.06 Fase 06 - Inicio de la dispersión bancaria de cierre a partir
 * de la propuesta de pago automática ya concluida en la Fase 05.
 */
@Service
public class IniciarDispersionBancariaUseCase {

    private final IDispersionBancariaCierreRepository dispersionBancariaCierreRepository;
    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public IniciarDispersionBancariaUseCase(IDispersionBancariaCierreRepository dispersionBancariaCierreRepository,
                                             IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.dispersionBancariaCierreRepository = dispersionBancariaCierreRepository;
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    @Transactional
    public DispersionBancariaCierreResponse ejecutar(IniciarDispersionBancariaRequest request) {
        PropuestaPagoAutomatica propuesta = propuestaPagoAutomaticaRepository
                .findById(request.getPropuestaPagoAutomaticaId())
                .orElseThrow(() -> new BadRequestException(
                        "Propuesta de pago no encontrada: " + request.getPropuestaPagoAutomaticaId()));

        // RN-AP6-01: una propuesta de pago ya usada en una dispersión no puede reutilizarse en otra.
        if (dispersionBancariaCierreRepository.existsByPropuestaPagoAutomaticaId(propuesta.getId())) {
            throw new BadRequestException(
                    "La propuesta de pago " + propuesta.getNumero()
                            + " ya cuenta con una dispersión bancaria de cierre");
        }

        String numero = generarNumeroDispersion();
        DispersionBancariaCierre dispersion = DispersionBancariaCierre.iniciar(numero, propuesta);

        DispersionBancariaCierre guardada = dispersionBancariaCierreRepository.save(dispersion);
        return DispersionBancariaCierreResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroDispersion() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "DBC-" + Year.now().getValue() + "-" + correlativo;
    }
}
