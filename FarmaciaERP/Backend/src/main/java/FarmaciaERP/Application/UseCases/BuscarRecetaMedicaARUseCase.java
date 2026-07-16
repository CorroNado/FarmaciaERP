package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarRecetaMedicaARUseCase {

    private final IRecetaMedicaARRepository recetaMedicaARRepository;

    public BuscarRecetaMedicaARUseCase(IRecetaMedicaARRepository recetaMedicaARRepository) {
        this.recetaMedicaARRepository = recetaMedicaARRepository;
    }

    public Optional<RecetaMedicaARResponse> porId(Long id) {
        return recetaMedicaARRepository.findById(id).map(RecetaMedicaARResponseAssembler::toResponse);
    }

    /**
     * RN-AR3-01: lista las recetas del lote y si todas ya alcanzaron un
     * estado terminal, indica que el ciclo puede continuar a la Fase 04.
     */
    public List<RecetaMedicaARResponse> porContabilizacionAR(Long contabilizacionARId) {
        return recetaMedicaARRepository.findByContabilizacionARId(contabilizacionARId).stream()
                .map(RecetaMedicaARResponseAssembler::toResponse)
                .toList();
    }

    public boolean puedeContinuarFase04(Long contabilizacionARId) {
        List<RecetaMedicaARResponse> recetas = porContabilizacionAR(contabilizacionARId);
        return !recetas.isEmpty() && recetas.stream().allMatch(RecetaMedicaARResponse::isProcesada);
    }

    public List<RecetaMedicaARResponse> todas() {
        return recetaMedicaARRepository.findAll().stream()
                .map(RecetaMedicaARResponseAssembler::toResponse)
                .toList();
    }
}
