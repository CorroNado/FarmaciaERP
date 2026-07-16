package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.InspeccionCalidadResponse;
import FarmaciaERP.Domain.Repositories.IInspeccionCalidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarInspeccionCalidadUseCase {

    private final IInspeccionCalidadRepository inspeccionCalidadRepository;

    public BuscarInspeccionCalidadUseCase(IInspeccionCalidadRepository inspeccionCalidadRepository) {
        this.inspeccionCalidadRepository = inspeccionCalidadRepository;
    }

    public Optional<InspeccionCalidadResponse> porId(Long id) {
        return inspeccionCalidadRepository.findById(id).map(InspeccionCalidadResponseAssembler::toResponse);
    }

    public List<InspeccionCalidadResponse> todas() {
        return inspeccionCalidadRepository.findAll().stream()
                .map(InspeccionCalidadResponseAssembler::toResponse).toList();
    }

    public List<InspeccionCalidadResponse> porEntradaMercancia(Long entradaMercanciaId) {
        return inspeccionCalidadRepository.findByEntradaMercanciaId(entradaMercanciaId).stream()
                .map(InspeccionCalidadResponseAssembler::toResponse).toList();
    }
}
