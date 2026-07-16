package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarCentroCostoUseCase {

    private final ICentroCostoRepository centroCostoRepository;

    public BuscarCentroCostoUseCase(ICentroCostoRepository centroCostoRepository) {
        this.centroCostoRepository = centroCostoRepository;
    }

    public Optional<CentroCostoResponse> porId(Long id) {
        return centroCostoRepository.findById(id).map(CentroCostoResponseAssembler::toResponse);
    }

    public List<CentroCostoResponse> todos() {
        return centroCostoRepository.findAll().stream().map(CentroCostoResponseAssembler::toResponse).toList();
    }

    public List<CentroCostoResponse> activos() {
        return centroCostoRepository.findAllActivos().stream().map(CentroCostoResponseAssembler::toResponse).toList();
    }
}