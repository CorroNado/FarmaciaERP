package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Domain.Repositories.ICompensacionARRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * FI-AR · Fase 06 - Consultas del ciclo de Compensación Final y
 * Análisis de Margen Neto, incluida la verificación de cierre del
 * ciclo FI-AR del período (RN-AR6-01).
 */
@Service
public class BuscarCompensacionARUseCase {

    private final ICompensacionARRepository compensacionARRepository;

    public BuscarCompensacionARUseCase(ICompensacionARRepository compensacionARRepository) {
        this.compensacionARRepository = compensacionARRepository;
    }

    public Optional<CompensacionARResponse> porId(Long id) {
        return compensacionARRepository.findById(id).map(CompensacionARResponseAssembler::toResponse);
    }

    public Optional<CompensacionARResponse> porContabilizacionAR(Long contabilizacionARId) {
        return compensacionARRepository.findByContabilizacionARId(contabilizacionARId)
                .map(CompensacionARResponseAssembler::toResponse);
    }

    public List<CompensacionARResponse> todos() {
        return compensacionARRepository.findAll().stream()
                .map(CompensacionARResponseAssembler::toResponse)
                .toList();
    }

    /**
     * RN-AR6-01: el ciclo FI-AR del lote queda finalizado cuando se
     * completó el cierre de ingresos por convenio.
     */
    public boolean cicloFinalizado(Long contabilizacionARId) {
        return porContabilizacionAR(contabilizacionARId)
                .map(CompensacionARResponse::isFinalizado)
                .orElse(false);
    }
}
