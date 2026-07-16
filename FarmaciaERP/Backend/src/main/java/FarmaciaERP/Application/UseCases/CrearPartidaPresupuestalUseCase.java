package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearPartidaPresupuestalRequest;
import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Entities.PartidaPresupuestal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Domain.Repositories.IPartidaPresupuestalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearPartidaPresupuestalUseCase {

    private final IPartidaPresupuestalRepository partidaRepository;
    private final ICentroCostoRepository centroCostoRepository;

    public CrearPartidaPresupuestalUseCase(IPartidaPresupuestalRepository partidaRepository,
                                            ICentroCostoRepository centroCostoRepository) {
        this.partidaRepository = partidaRepository;
        this.centroCostoRepository = centroCostoRepository;
    }

    @Transactional
    public PartidaPresupuestalResponse ejecutar(CrearPartidaPresupuestalRequest request) {
        if (partidaRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException(
                    "Ya existe una partida presupuestal registrada con el cÃ³digo " + request.getCodigo());
        }
        CentroCosto centroCosto = centroCostoRepository.findById(request.getCentroCostoId())
                .orElseThrow(() -> new BadRequestException(
                        "Centro de costo no encontrado: " + request.getCentroCostoId()));

        PartidaPresupuestal partida = new PartidaPresupuestal(request.getCodigo(), request.getNombre(), centroCosto,
                request.getMontoPresupuestado());
        return PartidaPresupuestalResponseAssembler.toResponse(partidaRepository.save(partida));
    }
}