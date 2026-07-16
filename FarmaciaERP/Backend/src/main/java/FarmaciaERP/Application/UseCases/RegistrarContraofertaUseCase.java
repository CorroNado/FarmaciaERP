package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarContraofertaRequest;
import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Entities.DisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.02 Fase 02 - paso 2.2.1 (cont.): el laboratorio/droguería envía la
 * contrapropuesta de la ronda de negociación vigente.
 */
@Service
public class RegistrarContraofertaUseCase {

    private final IDisputaComercialRepository disputaComercialRepository;

    public RegistrarContraofertaUseCase(IDisputaComercialRepository disputaComercialRepository) {
        this.disputaComercialRepository = disputaComercialRepository;
    }

    @Transactional
    public DisputaComercialResponse ejecutar(Long disputaComercialId, RegistrarContraofertaRequest request) {
        DisputaComercial disputa = disputaComercialRepository.findById(disputaComercialId)
                .orElseThrow(() -> new BadRequestException("Disputa comercial no encontrada: " + disputaComercialId));

        disputa.registrarContraoferta(request.getMontoContraoferta());

        DisputaComercial guardada = disputaComercialRepository.save(disputa);
        return DisputaComercialResponseAssembler.toResponse(guardada);
    }
}
