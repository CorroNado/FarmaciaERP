package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 02 - 2.2.2 Procesar Asiento Automatizado de Ventas y
 * Cuadraturas (RN-AR2-01).
 */
@Service
public class ProcesarAsientoContableUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;

    public ProcesarAsientoContableUseCase(IContabilizacionARRepository contabilizacionARRepository) {
        this.contabilizacionARRepository = contabilizacionARRepository;
    }

    @Transactional
    public ContabilizacionARResponse ejecutar(Long id) {
        ContabilizacionAR contabilizacion = contabilizacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contabilización AR no encontrada: " + id));

        contabilizacion.procesarAsientoContable();

        ContabilizacionAR guardada = contabilizacionARRepository.save(contabilizacion);
        return ContabilizacionARResponseAssembler.toResponse(guardada);
    }
}
