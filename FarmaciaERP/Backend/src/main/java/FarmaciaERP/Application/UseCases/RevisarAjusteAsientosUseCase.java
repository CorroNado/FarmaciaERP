package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 02 - 2.2.3 Revisión y Ajuste de Asientos Descuadrados
 * (RN-AR2-01).
 */
@Service
public class RevisarAjusteAsientosUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;

    public RevisarAjusteAsientosUseCase(IContabilizacionARRepository contabilizacionARRepository) {
        this.contabilizacionARRepository = contabilizacionARRepository;
    }

    @Transactional
    public ContabilizacionARResponse ejecutar(Long id) {
        ContabilizacionAR contabilizacion = contabilizacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contabilización AR no encontrada: " + id));

        contabilizacion.revisarAjusteAsientos();

        ContabilizacionAR guardada = contabilizacionARRepository.save(contabilizacion);
        return ContabilizacionARResponseAssembler.toResponse(guardada);
    }
}
