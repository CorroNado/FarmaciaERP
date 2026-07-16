package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarAjusteContableUseCase {

    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;

    public BuscarAjusteContableUseCase(IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository) {
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
    }

    public Optional<AjusteContableRegularizacionResponse> porId(Long id) {
        return ajusteContableRegularizacionRepository.findById(id)
                .map(AjusteContableRegularizacionResponseAssembler::toResponse);
    }

    public List<AjusteContableRegularizacionResponse> todos() {
        return ajusteContableRegularizacionRepository.findAll().stream()
                .map(AjusteContableRegularizacionResponseAssembler::toResponse).toList();
    }

    public List<AjusteContableRegularizacionResponse> porDisputaComercial(Long disputaComercialId) {
        return ajusteContableRegularizacionRepository.findByDisputaComercialId(disputaComercialId).stream()
                .map(AjusteContableRegularizacionResponseAssembler::toResponse).toList();
    }
}
