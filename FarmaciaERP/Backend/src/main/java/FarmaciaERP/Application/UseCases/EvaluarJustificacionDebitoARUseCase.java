package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.EvaluarJustificacionDebitoARRequest;
import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Domain.Entities.DebitoAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDebitoARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 04 - Evaluación de si el débito calculado está
 * justificado; de estarlo, Contabilidad registra el débito, de lo
 * contrario debe tramitarse el reclamo (RN-AR4-01).
 */
@Service
public class EvaluarJustificacionDebitoARUseCase {

    private final IDebitoARRepository debitoARRepository;

    public EvaluarJustificacionDebitoARUseCase(IDebitoARRepository debitoARRepository) {
        this.debitoARRepository = debitoARRepository;
    }

    @Transactional
    public DebitoARResponse ejecutar(Long id, EvaluarJustificacionDebitoARRequest request) {
        DebitoAR debito = debitoARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Débito AR no encontrado: " + id));

        debito.evaluarJustificacion(request.isJustificado());

        DebitoAR guardado = debitoARRepository.save(debito);
        return DebitoARResponseAssembler.toResponse(guardado);
    }
}
