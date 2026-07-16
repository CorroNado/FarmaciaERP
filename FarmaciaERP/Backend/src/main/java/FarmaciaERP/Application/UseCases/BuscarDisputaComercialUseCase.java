package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Domain.Repositories.IDisputaComercialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarDisputaComercialUseCase {

    private final IDisputaComercialRepository disputaComercialRepository;

    public BuscarDisputaComercialUseCase(IDisputaComercialRepository disputaComercialRepository) {
        this.disputaComercialRepository = disputaComercialRepository;
    }

    public Optional<DisputaComercialResponse> porId(Long id) {
        return disputaComercialRepository.findById(id).map(DisputaComercialResponseAssembler::toResponse);
    }

    public List<DisputaComercialResponse> todas() {
        return disputaComercialRepository.findAll().stream()
                .map(DisputaComercialResponseAssembler::toResponse).toList();
    }

    public List<DisputaComercialResponse> porExcepcionFacturacion(Long excepcionFacturacionId) {
        return disputaComercialRepository.findByExcepcionFacturacionId(excepcionFacturacionId).stream()
                .map(DisputaComercialResponseAssembler::toResponse).toList();
    }
}
