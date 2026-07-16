package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.02 Fase 02 - paso 2.2.2: ¿Se Absorbe la Diferencia Comercial? — Sí.
 */
@Service
public class AceptarAbsorcionUseCase {

    private final IDisputaComercialRepository disputaComercialRepository;

    public AceptarAbsorcionUseCase(IDisputaComercialRepository disputaComercialRepository) {
        this.disputaComercialRepository = disputaComercialRepository;
    }

    @Transactional
    public DisputaComercialResponse ejecutar(Long disputaComercialId) {
        DisputaComercial disputa = disputaComercialRepository.findById(disputaComercialId)
                .orElseThrow(() -> new BadRequestException("Disputa comercial no encontrada: " + disputaComercialId));

        disputa.aceptarAbsorcion();

        DisputaComercial guardada = disputaComercialRepository.save(disputa);
        return DisputaComercialResponseAssembler.toResponse(guardada);
    }
}
