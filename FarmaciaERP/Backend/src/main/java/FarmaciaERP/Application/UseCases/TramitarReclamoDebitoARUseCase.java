package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Domain.Entities.DebitoAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDebitoARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 04 - Tramitación del reclamo ante el Área Técnica para
 * los débitos no justificados (RN-AR4-01).
 */
@Service
public class TramitarReclamoDebitoARUseCase {

    private final IDebitoARRepository debitoARRepository;

    public TramitarReclamoDebitoARUseCase(IDebitoARRepository debitoARRepository) {
        this.debitoARRepository = debitoARRepository;
    }

    @Transactional
    public DebitoARResponse ejecutar(Long id) {
        DebitoAR debito = debitoARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Débito AR no encontrado: " + id));

        debito.tramitarReclamo();

        DebitoAR guardado = debitoARRepository.save(debito);
        return DebitoARResponseAssembler.toResponse(guardado);
    }
}
