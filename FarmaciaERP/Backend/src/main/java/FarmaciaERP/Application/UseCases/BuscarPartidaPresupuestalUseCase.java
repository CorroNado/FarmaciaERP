package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Domain.Repositories.IPartidaPresupuestalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarPartidaPresupuestalUseCase {

    private final IPartidaPresupuestalRepository partidaRepository;

    public BuscarPartidaPresupuestalUseCase(IPartidaPresupuestalRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    public Optional<PartidaPresupuestalResponse> porId(Long id) {
        return partidaRepository.findById(id).map(PartidaPresupuestalResponseAssembler::toResponse);
    }

    public List<PartidaPresupuestalResponse> todas() {
        return partidaRepository.findAll().stream().map(PartidaPresupuestalResponseAssembler::toResponse).toList();
    }

    public List<PartidaPresupuestalResponse> porCentroCosto(Long centroCostoId) {
        return partidaRepository.findByCentroCostoId(centroCostoId).stream()
                .map(PartidaPresupuestalResponseAssembler::toResponse).toList();
    }
}