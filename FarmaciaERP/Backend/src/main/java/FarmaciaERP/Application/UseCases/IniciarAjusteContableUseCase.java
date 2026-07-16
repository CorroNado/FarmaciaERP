package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.IniciarAjusteContableRequest;
import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FI-AP.03 Fase 03 - Inicio del cierre de transacción: inicia el ajuste
 * contable y de regularización a partir de una disputa comercial ya
 * resuelta en el workflow del ERP (Fase 02).
 */
@Service
public class IniciarAjusteContableUseCase {

    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;
    private final IDisputaComercialRepository disputaComercialRepository;

    public IniciarAjusteContableUseCase(IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository,
                                         IDisputaComercialRepository disputaComercialRepository) {
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
        this.disputaComercialRepository = disputaComercialRepository;
    }

    @Transactional
    public AjusteContableRegularizacionResponse ejecutar(IniciarAjusteContableRequest request) {
        if (request.getDisputaComercialId() == null) {
            throw new BadRequestException("El identificador de la disputa comercial es obligatorio");
        }

        DisputaComercial disputa = disputaComercialRepository.findById(request.getDisputaComercialId())
                .orElseThrow(() -> new BadRequestException(
                        "Disputa comercial no encontrada: " + request.getDisputaComercialId()));

        // RN-AP3-01: no debe existir ya un ajuste contable iniciado para la misma disputa.
        if (ajusteContableRegularizacionRepository.existsByDisputaComercialId(disputa.getId())) {
            throw new BadRequestException(
                    "Ya existe un ajuste contable y de regularización iniciado para la disputa " + disputa.getNumero());
        }

        String numero = generarNumeroAjuste();
        AjusteContableRegularizacion ajuste = AjusteContableRegularizacion.iniciar(numero, disputa);

        AjusteContableRegularizacion guardado = ajusteContableRegularizacionRepository.save(ajuste);
        return AjusteContableRegularizacionResponseAssembler.toResponse(guardado);
    }

    private String generarNumeroAjuste() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "AJC-AP-" + Year.now().getValue() + "-" + correlativo;
    }
}
