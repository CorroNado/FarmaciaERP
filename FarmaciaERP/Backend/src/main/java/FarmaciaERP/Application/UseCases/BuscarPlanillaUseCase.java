package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PlanillaResponse;
import FarmaciaERP.Application.DTOs.Response.PlanillaResumenResponse;
import FarmaciaERP.Domain.Repositories.IPlanillaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarPlanillaUseCase {

    private final IPlanillaRepository planillaRepository;

    public BuscarPlanillaUseCase(IPlanillaRepository planillaRepository) {
        this.planillaRepository = planillaRepository;
    }

    public Optional<PlanillaResponse> porId(Long id) {
        return planillaRepository.findById(id).map(PlanillaResponseAssembler::toResponse);
    }

    public Optional<PlanillaResponse> porMesYAnio(int mes, int anio) {
        return planillaRepository.findByMesAndAnio(mes, anio).map(PlanillaResponseAssembler::toResponse);
    }

    /** Historial de planillas guardadas ("Planillas Guardadas" del prototipo). */
    public List<PlanillaResumenResponse> historial() {
        return planillaRepository.findAll().stream()
                .map(PlanillaResponseAssembler::toResumen).toList();
    }
}
