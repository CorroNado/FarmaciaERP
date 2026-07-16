package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Domain.Repositories.IDebitoARRepository;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * FI-AR · Fase 04 - Consultas del ciclo de Conciliación de Débitos y
 * Ajustes Técnicos, incluida la verificación de habilitación de la
 * Fase 05 (RN-AR4-01).
 */
@Service
public class BuscarDebitoARUseCase {

    private final IDebitoARRepository debitoARRepository;
    private final IRecetaMedicaARRepository recetaMedicaARRepository;

    public BuscarDebitoARUseCase(IDebitoARRepository debitoARRepository,
                                  IRecetaMedicaARRepository recetaMedicaARRepository) {
        this.debitoARRepository = debitoARRepository;
        this.recetaMedicaARRepository = recetaMedicaARRepository;
    }

    public Optional<DebitoARResponse> porId(Long id) {
        return debitoARRepository.findById(id).map(DebitoARResponseAssembler::toResponse);
    }

    public List<DebitoARResponse> porContabilizacionAR(Long contabilizacionARId) {
        return debitoARRepository.findByContabilizacionARId(contabilizacionARId).stream()
                .map(DebitoARResponseAssembler::toResponse)
                .toList();
    }

    public List<DebitoARResponse> todos() {
        return debitoARRepository.findAll().stream()
                .map(DebitoARResponseAssembler::toResponse)
                .toList();
    }

    /**
     * RN-AR4-01: si la Fase 03 no generó débitos para el lote, o si
     * todos los débitos generados ya fueron conciliados (ajustados), el
     * ciclo puede continuar a la Fase 05.
     */
    public boolean puedeContinuarFase05(Long contabilizacionARId) {
        boolean sinDebitosPendientes = recetaMedicaARRepository.findByContabilizacionARId(contabilizacionARId)
                .stream()
                .noneMatch(r -> r.generaDebito());

        List<DebitoARResponse> debitos = porContabilizacionAR(contabilizacionARId);
        boolean todosConciliados = !debitos.isEmpty() && debitos.stream().allMatch(DebitoARResponse::isConciliado);

        return sinDebitosPendientes || todosConciliados;
    }

    /**
     * RN-AR4-01: total de ajustes técnicos contables aplicados sobre el
     * lote, usado en el reporte de la Fase 06.
     */
    public double ajusteTotal(Long contabilizacionARId) {
        return porContabilizacionAR(contabilizacionARId).stream()
                .filter(DebitoARResponse::isAjustado)
                .mapToDouble(DebitoARResponse::getMonto)
                .sum();
    }
}
