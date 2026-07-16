package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ConciliacionTresViasResponse;
import FarmaciaERP.Domain.Repositories.IConciliacionTresViasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarConciliacionTresViasUseCase {

    private final IConciliacionTresViasRepository conciliacionTresViasRepository;

    public BuscarConciliacionTresViasUseCase(IConciliacionTresViasRepository conciliacionTresViasRepository) {
        this.conciliacionTresViasRepository = conciliacionTresViasRepository;
    }

    public Optional<ConciliacionTresViasResponse> porId(Long id) {
        return conciliacionTresViasRepository.findById(id).map(ConciliacionTresViasResponseAssembler::toResponse);
    }

    public List<ConciliacionTresViasResponse> todas() {
        return conciliacionTresViasRepository.findAll().stream()
                .map(ConciliacionTresViasResponseAssembler::toResponse).toList();
    }

    public List<ConciliacionTresViasResponse> porOrdenCompra(Long ordenCompraId) {
        return conciliacionTresViasRepository.findByOrdenCompraId(ordenCompraId).stream()
                .map(ConciliacionTresViasResponseAssembler::toResponse).toList();
    }
}
