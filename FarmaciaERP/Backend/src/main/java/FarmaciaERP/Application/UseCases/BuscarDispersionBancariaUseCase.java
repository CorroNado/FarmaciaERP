package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Domain.Repositories.IDispersionBancariaCierreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarDispersionBancariaUseCase {

    private final IDispersionBancariaCierreRepository dispersionBancariaCierreRepository;

    public BuscarDispersionBancariaUseCase(IDispersionBancariaCierreRepository dispersionBancariaCierreRepository) {
        this.dispersionBancariaCierreRepository = dispersionBancariaCierreRepository;
    }

    public Optional<DispersionBancariaCierreResponse> porId(Long id) {
        return dispersionBancariaCierreRepository.findById(id)
                .map(DispersionBancariaCierreResponseAssembler::toResponse);
    }

    public List<DispersionBancariaCierreResponse> todos() {
        return dispersionBancariaCierreRepository.findAll().stream()
                .map(DispersionBancariaCierreResponseAssembler::toResponse).toList();
    }
}
