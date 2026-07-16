package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.AuditarRecetasRequest;
import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 02 - 2.3.1 Auditoría de Integridad y Firmas de Recetas
 * Médicas, cruzada contra la pre-liquidación de la aseguradora
 * (RN-AR2-01).
 */
@Service
public class AuditarRecetasUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;

    public AuditarRecetasUseCase(IContabilizacionARRepository contabilizacionARRepository) {
        this.contabilizacionARRepository = contabilizacionARRepository;
    }

    @Transactional
    public ContabilizacionARResponse ejecutar(Long id, AuditarRecetasRequest request) {
        ContabilizacionAR contabilizacion = contabilizacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contabilización AR no encontrada: " + id));

        contabilizacion.auditarRecetas(request.isConforme(), request.getMotivoObservacion());

        ContabilizacionAR guardada = contabilizacionARRepository.save(contabilizacion);
        return ContabilizacionARResponseAssembler.toResponse(guardada);
    }
}
