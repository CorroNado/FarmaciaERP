package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BuscarAsientoContableUseCase {

    private final IAsientoContableRepository asientoRepository;

    public BuscarAsientoContableUseCase(IAsientoContableRepository asientoRepository) {
        this.asientoRepository = asientoRepository;
    }

    public Optional<AsientoContableResponse> porId(Long id) {
        return asientoRepository.findById(id).map(AsientoContableResponseAssembler::toResponse);
    }

    public Optional<AsientoContableResponse> porNumero(String numero) {
        return asientoRepository.findByNumero(numero).map(AsientoContableResponseAssembler::toResponse);
    }

    public List<AsientoContableResponse> todos() {
        return asientoRepository.findAll().stream().map(AsientoContableResponseAssembler::toResponse).toList();
    }

    public List<AsientoContableResponse> porEstado(EstadoAsiento estado) {
        return asientoRepository.findByEstado(estado).stream().map(AsientoContableResponseAssembler::toResponse).toList();
    }

    public List<AsientoContableResponse> porRangoFechas(LocalDate inicio, LocalDate fin) {
        return asientoRepository.findByFechaContableBetween(inicio, fin).stream()
                .map(AsientoContableResponseAssembler::toResponse).toList();
    }
}