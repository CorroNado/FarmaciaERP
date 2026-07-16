package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Domain.Entities.DebitoAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDebitoARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 04 - Aplicación del Ajuste Técnico Contable que cierra
 * el ciclo del débito: registro contable del débito justificado, o
 * pérdida contable del reclamo tramitado sin éxito (RN-AR4-01).
 */
@Service
public class AplicarAjusteTecnicoContableUseCase {

    private final IDebitoARRepository debitoARRepository;

    public AplicarAjusteTecnicoContableUseCase(IDebitoARRepository debitoARRepository) {
        this.debitoARRepository = debitoARRepository;
    }

    @Transactional
    public DebitoARResponse ejecutar(Long id) {
        DebitoAR debito = debitoARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Débito AR no encontrado: " + id));

        debito.aplicarAjusteTecnicoContable();

        DebitoAR guardado = debitoARRepository.save(debito);
        return DebitoARResponseAssembler.toResponse(guardado);
    }
}
