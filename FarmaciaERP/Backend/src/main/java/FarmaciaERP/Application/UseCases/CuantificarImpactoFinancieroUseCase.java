package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.02 Fase 02 - paso 2.1.2: Cuantificación del Impacto Financiero de
 * la Desviación.
 */
@Service
public class CuantificarImpactoFinancieroUseCase {

    private final IDisputaComercialRepository disputaComercialRepository;

    public CuantificarImpactoFinancieroUseCase(IDisputaComercialRepository disputaComercialRepository) {
        this.disputaComercialRepository = disputaComercialRepository;
    }

    @Transactional
    public DisputaComercialResponse ejecutar(Long disputaComercialId) {
        DisputaComercial disputa = disputaComercialRepository.findById(disputaComercialId)
                .orElseThrow(() -> new BadRequestException("Disputa comercial no encontrada: " + disputaComercialId));

        disputa.cuantificarImpactoFinanciero();

        DisputaComercial guardada = disputaComercialRepository.save(disputa);
        return DisputaComercialResponseAssembler.toResponse(guardada);
    }
}
