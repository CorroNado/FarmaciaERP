package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarSubcuentaDivisionariaUseCase {

    private final ISubcuentaDivisionariaRepository subcuentaRepository;

    public BuscarSubcuentaDivisionariaUseCase(ISubcuentaDivisionariaRepository subcuentaRepository) {
        this.subcuentaRepository = subcuentaRepository;
    }

    public Optional<SubcuentaDivisionariaResponse> porId(Long id) {
        return subcuentaRepository.findById(id).map(SubcuentaDivisionariaResponseAssembler::toResponse);
    }

    public List<SubcuentaDivisionariaResponse> todas() {
        return subcuentaRepository.findAll().stream().map(SubcuentaDivisionariaResponseAssembler::toResponse).toList();
    }

    public List<SubcuentaDivisionariaResponse> porCuenta(Long cuentaId) {
        return subcuentaRepository.findByCuentaId(cuentaId).stream()
                .map(SubcuentaDivisionariaResponseAssembler::toResponse).toList();
    }
}