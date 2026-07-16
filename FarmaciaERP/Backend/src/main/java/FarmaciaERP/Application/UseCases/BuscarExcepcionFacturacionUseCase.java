package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ExcepcionFacturacionResponse;
import FarmaciaERP.Domain.Repositories.IExcepcionFacturacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarExcepcionFacturacionUseCase {

    private final IExcepcionFacturacionRepository excepcionFacturacionRepository;

    public BuscarExcepcionFacturacionUseCase(IExcepcionFacturacionRepository excepcionFacturacionRepository) {
        this.excepcionFacturacionRepository = excepcionFacturacionRepository;
    }

    public Optional<ExcepcionFacturacionResponse> porId(Long id) {
        return excepcionFacturacionRepository.findById(id).map(ExcepcionFacturacionResponseAssembler::toResponse);
    }

    public List<ExcepcionFacturacionResponse> todas() {
        return excepcionFacturacionRepository.findAll().stream()
                .map(ExcepcionFacturacionResponseAssembler::toResponse).toList();
    }

    public List<ExcepcionFacturacionResponse> porConciliacionTresVias(Long conciliacionTresViasId) {
        return excepcionFacturacionRepository.findByConciliacionTresViasId(conciliacionTresViasId).stream()
                .map(ExcepcionFacturacionResponseAssembler::toResponse).toList();
    }
}
