package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.AbrirDisputaComercialRequest;
import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import FarmaciaERP.Domain.Repositories.IExcepcionFacturacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FI-AP.02 Fase 02 - Notificación de discrepancia recibida (Sistema ERP
 * Central): abre la disputa comercial a partir de una excepción de
 * facturación ya clasificada y notificada en la Fase 01.
 */
@Service
public class AbrirDisputaComercialUseCase {

    private final IDisputaComercialRepository disputaComercialRepository;
    private final IExcepcionFacturacionRepository excepcionFacturacionRepository;

    public AbrirDisputaComercialUseCase(IDisputaComercialRepository disputaComercialRepository,
                                         IExcepcionFacturacionRepository excepcionFacturacionRepository) {
        this.disputaComercialRepository = disputaComercialRepository;
        this.excepcionFacturacionRepository = excepcionFacturacionRepository;
    }

    @Transactional
    public DisputaComercialResponse ejecutar(AbrirDisputaComercialRequest request) {
        if (request.getExcepcionFacturacionId() == null) {
            throw new BadRequestException("El identificador de la excepción de facturación es obligatorio");
        }

        ExcepcionFacturacion excepcion = excepcionFacturacionRepository.findById(request.getExcepcionFacturacionId())
                .orElseThrow(() -> new BadRequestException(
                        "Excepción de facturación no encontrada: " + request.getExcepcionFacturacionId()));

        // RN-AP2-01: no debe existir ya una disputa comercial abierta para la misma excepción.
        if (disputaComercialRepository.existsByExcepcionFacturacionId(excepcion.getId())) {
            throw new BadRequestException(
                    "Ya existe una disputa comercial abierta para la excepción " + excepcion.getNumero());
        }

        String numero = generarNumeroDisputa();
        DisputaComercial disputa = DisputaComercial.abrir(numero, excepcion);

        DisputaComercial guardada = disputaComercialRepository.save(disputa);
        return DisputaComercialResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroDisputa() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "DISP-AP-" + Year.now().getValue() + "-" + correlativo;
    }
}
