package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Domain.Repositories.ICobroARRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * FI-AR · Fase 05 - Consultas del ciclo de Procesamiento de Cobros e
 * Imputación Bancaria, incluida la verificación de habilitación de la
 * Fase 06 (RN-AR5-01).
 */
@Service
public class BuscarCobroARUseCase {

    private final ICobroARRepository cobroARRepository;

    public BuscarCobroARUseCase(ICobroARRepository cobroARRepository) {
        this.cobroARRepository = cobroARRepository;
    }

    public Optional<CobroARResponse> porId(Long id) {
        return cobroARRepository.findById(id).map(CobroARResponseAssembler::toResponse);
    }

    public Optional<CobroARResponse> porContabilizacionAR(Long contabilizacionARId) {
        return cobroARRepository.findByContabilizacionARId(contabilizacionARId)
                .map(CobroARResponseAssembler::toResponse);
    }

    public List<CobroARResponse> todos() {
        return cobroARRepository.findAll().stream()
                .map(CobroARResponseAssembler::toResponse)
                .toList();
    }

    /**
     * RN-AR5-01: el lote puede continuar a la Fase 06 solo cuando el
     * ingreso de dinero fue registrado e imputado en la cuenta del
     * cliente.
     */
    public boolean puedeContinuarFase06(Long contabilizacionARId) {
        return porContabilizacionAR(contabilizacionARId)
                .map(CobroARResponse::isPuedeContinuarFase06)
                .orElse(false);
    }
}
