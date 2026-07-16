package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Domain.Repositories.IPropuestaPagoAutomaticaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarPropuestaPagoUseCase {

    private final IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository;

    public BuscarPropuestaPagoUseCase(IPropuestaPagoAutomaticaRepository propuestaPagoAutomaticaRepository) {
        this.propuestaPagoAutomaticaRepository = propuestaPagoAutomaticaRepository;
    }

    public Optional<PropuestaPagoAutomaticaResponse> porId(Long id) {
        return propuestaPagoAutomaticaRepository.findById(id).map(PropuestaPagoAutomaticaResponseAssembler::toResponse);
    }

    public List<PropuestaPagoAutomaticaResponse> todos() {
        return propuestaPagoAutomaticaRepository.findAll().stream()
                .map(PropuestaPagoAutomaticaResponseAssembler::toResponse).toList();
    }
}
