package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.OrdenTrasladoResponse;
import FarmaciaERP.Domain.Repositories.IOrdenTrasladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarOrdenTrasladoUseCase {

    private final IOrdenTrasladoRepository ordenTrasladoRepository;

    public BuscarOrdenTrasladoUseCase(IOrdenTrasladoRepository ordenTrasladoRepository) {
        this.ordenTrasladoRepository = ordenTrasladoRepository;
    }

    public Optional<OrdenTrasladoResponse> porId(Long id) {
        return ordenTrasladoRepository.findById(id).map(OrdenTrasladoResponseAssembler::toResponse);
    }

    public List<OrdenTrasladoResponse> todas() {
        return ordenTrasladoRepository.findAll().stream().map(OrdenTrasladoResponseAssembler::toResponse).toList();
    }

    public List<OrdenTrasladoResponse> porInspeccionCalidad(Long inspeccionCalidadId) {
        return ordenTrasladoRepository.findByInspeccionCalidadId(inspeccionCalidadId).stream()
                .map(OrdenTrasladoResponseAssembler::toResponse).toList();
    }

    public List<OrdenTrasladoResponse> porSucursal(Long sucursalId) {
        return ordenTrasladoRepository.findBySucursalDestinoId(sucursalId).stream()
                .map(OrdenTrasladoResponseAssembler::toResponse).toList();
    }
}
