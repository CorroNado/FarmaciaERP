package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.02 Fase 02 - paso 2.3.1: Registrar Resolución en Workflow del ERP.
 * RN-AP2-08: concluye la Fase 02 y habilita la Fase 03 (Ajustes Contables y
 * Regularización).
 */
@Service
public class ResolverWorkflowDisputaUseCase {

    private final IDisputaComercialRepository disputaComercialRepository;

    public ResolverWorkflowDisputaUseCase(IDisputaComercialRepository disputaComercialRepository) {
        this.disputaComercialRepository = disputaComercialRepository;
    }

    @Transactional
    public DisputaComercialResponse ejecutar(Long disputaComercialId) {
        DisputaComercial disputa = disputaComercialRepository.findById(disputaComercialId)
                .orElseThrow(() -> new BadRequestException("Disputa comercial no encontrada: " + disputaComercialId));

        disputa.resolverWorkflow();

        DisputaComercial guardada = disputaComercialRepository.save(disputa);
        return DisputaComercialResponseAssembler.toResponse(guardada);
    }
}
