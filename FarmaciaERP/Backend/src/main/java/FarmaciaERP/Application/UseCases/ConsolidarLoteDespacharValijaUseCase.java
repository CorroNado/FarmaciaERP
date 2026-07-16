package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 02 - 2.3.2 Consolidación del Lote y Despacho de Valija
 * Física hacia Oficina Central. Al completarse, habilita la Fase 03 —
 * Auditoría Médica e Impugnación de Recetas (RN-AR2-01).
 */
@Service
public class ConsolidarLoteDespacharValijaUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;

    public ConsolidarLoteDespacharValijaUseCase(IContabilizacionARRepository contabilizacionARRepository) {
        this.contabilizacionARRepository = contabilizacionARRepository;
    }

    @Transactional
    public ContabilizacionARResponse ejecutar(Long id) {
        ContabilizacionAR contabilizacion = contabilizacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contabilización AR no encontrada: " + id));

        contabilizacion.consolidarLoteDespacharValija();

        ContabilizacionAR guardada = contabilizacionARRepository.save(contabilizacion);
        return ContabilizacionARResponseAssembler.toResponse(guardada);
    }
}
